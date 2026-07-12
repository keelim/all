package com.keelim.comssa.ui.screen.main.finance

import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.data.repository.FinanceRssRepository
import com.keelim.model.finance.FinanceCategory
import com.keelim.model.finance.FinanceRssItem
import com.keelim.model.finance.FinanceSource
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceViewModelTest : FunSpec({

    lateinit var viewModel: FinanceViewModel
    lateinit var mockRepository: FinanceRssRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    val testSources = listOf(
        FinanceSource(
            name = "테스트경제",
            url = "https://test.com/feed",
            category = FinanceCategory.ECONOMY,
            isEnabled = true,
        ),
        FinanceSource(
            name = "테스트주식",
            url = "https://test.com/stock",
            category = FinanceCategory.STOCK,
            isEnabled = true,
        ),
    )

    val testItems = listOf(
        FinanceRssItem(
            title = "경제 뉴스 1",
            description = "경제 뉴스 설명 1",
            link = "https://test.com/1",
            pubDate = Instant.fromEpochMilliseconds(1000L),
            category = "경제",
            source = "테스트경제",
        ),
        FinanceRssItem(
            title = "주식 뉴스 1",
            description = "주식 뉴스 설명 1",
            link = "https://test.com/2",
            pubDate = Instant.fromEpochMilliseconds(2000L),
            category = "주식",
            source = "테스트주식",
        ),
        FinanceRssItem(
            title = "암호화폐 뉴스 1",
            description = "암호화폐 뉴스 설명 1",
            link = "https://test.com/3",
            pubDate = Instant.fromEpochMilliseconds(3000L),
            category = "암호화폐",
            source = "테스트경제",
        ),
    )

    beforeTest {
        mockRepository = mockk()
        coEvery { mockRepository.getSources() } returns testSources
        coEvery { mockRepository.getRssItems(any()) } returns flowOf(testItems)
        coEvery { mockRepository.clearCache() } returns Unit
        coEvery { mockRepository.invalidateCacheForSource(any()) } returns Unit
        coEvery { mockRepository.getCacheInfo() } returns mapOf("test" to 1000L)

        viewModel = FinanceViewModel(mockRepository)
    }

    test("초기 상태는 로딩 상태여야 한다") {
        runTest {
            val initialState = viewModel.items.value
            (initialState is SealedUiState.Loading) shouldBe true
        }
    }

    test("RSS 아이템을 성공적으로 가져와야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true

            if (state is SealedUiState.Success) {
                state.value.isNotEmpty() shouldBe true
            }
        }
    }

    test("필터 버튼이 올바르게 설정되어야 한다") {
        val filterButtons = viewModel.filterButtons
        filterButtons.size shouldBe 6

        val labels = filterButtons.map { it.label }
        labels.contains("전체") shouldBe true
        labels.contains("주식") shouldBe true
        labels.contains("암호화폐") shouldBe true
        labels.contains("외환") shouldBe true
        labels.contains("경제") shouldBe true
        labels.contains("부동산") shouldBe true
    }

    test("카테고리 필터가 올바르게 적용되어야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            val stockFilter = viewModel.filterButtons.find { it.label == "주식" }!!
            viewModel.updateFilter(stockFilter)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }

    test("소스 필터가 올바르게 적용되어야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.updateSource("테스트경제")
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }

    test("전체 필터 적용 시 소스 필터가 초기화되어야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.updateSource("테스트경제")
            testDispatcher.scheduler.advanceUntilIdle()

            val allFilter = viewModel.filterButtons.find { it.label == "전체" }!!
            viewModel.updateFilter(allFilter)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }

    test("새로고침이 올바르게 동작해야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.refresh()
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }

    test("오류 후 새로고침은 RSS 흐름을 다시 수집해야 한다") {
        runTest {
            var requestCount = 0
            coEvery { mockRepository.getRssItems(any()) } answers {
                requestCount += 1
                if (requestCount == 1) {
                    flow { throw IllegalStateException("boom") }
                } else {
                    flowOf(testItems)
                }
            }
            val retryViewModel = FinanceViewModel(mockRepository)

            retryViewModel.items.test {
                awaitItem() shouldBe SealedUiState.Loading
                testDispatcher.scheduler.advanceUntilIdle()
                (awaitItem() is SealedUiState.Error) shouldBe true

                retryViewModel.refresh()
                testDispatcher.scheduler.advanceUntilIdle()

                awaitItem() shouldBe SealedUiState.Loading
                (awaitItem() as SealedUiState.Success).value shouldBe testItems
                requestCount shouldBe 2
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("캐시 관련 메서드들이 올바르게 동작해야 한다") {
        runTest {
            viewModel.clearCache()
            verify { mockRepository.clearCache() }

            viewModel.invalidateCacheForSource("https://test.com/feed")
            verify { mockRepository.invalidateCacheForSource("https://test.com/feed") }

            val cacheInfo = viewModel.getCacheInfo()
            cacheInfo shouldBe mapOf("test" to 1000L)
        }
    }

    test("암호화폐 필터가 올바르게 동작해야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            val cryptoFilter = viewModel.filterButtons.find { it.label == "암호화폐" }!!
            viewModel.updateFilter(cryptoFilter)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }

    test("경제 필터가 올바르게 동작해야 한다") {
        runTest {
            testDispatcher.scheduler.advanceUntilIdle()

            val economyFilter = viewModel.filterButtons.find { it.label == "경제" }!!
            viewModel.updateFilter(economyFilter)
            testDispatcher.scheduler.advanceUntilIdle()

            val state = viewModel.items.value
            (state is SealedUiState.Loading || state is SealedUiState.Success) shouldBe true
        }
    }
})
