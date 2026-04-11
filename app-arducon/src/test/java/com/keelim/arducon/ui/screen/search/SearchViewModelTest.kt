package com.keelim.arducon.ui.screen.search

import app.cash.turbine.test
import com.keelim.data.repository.ArduconRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest : FunSpec({

    lateinit var viewModel: SearchViewModel
    lateinit var mockRepository: ArduconRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val schemes = listOf(
        "market://details?id=com.example.app",
        "mailto:test@example.com",
        "geo:0,0?q=Market",
    )

    extension(mainDispatcherRule)

    beforeTest {
        mockRepository = mockk(relaxed = true)

        every { mockRepository.getSchemeList() } returns flowOf(schemes)

        viewModel = SearchViewModel(mockRepository, testDispatcher)
    }

    test("검색어 지우기 시 빈 문자열로 초기화되어야 한다") {
        runTest {
            viewModel.updateSearchQuery("test")
            viewModel.clearSearch()

            viewModel.searchQuery.test {
                awaitItem() shouldBe ""
            }
        }
    }

    test("검색어 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateSearchQuery("test")

            viewModel.searchQuery.test {
                awaitItem() shouldBe "test"
            }
        }
    }

    test("검색어가 비어 있으면 전체 스킴 목록을 노출해야 한다") {
        runTest {
            viewModel.filteredSchemes.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()
                awaitItem() shouldBe schemes
            }
        }
    }

    test("검색어에 맞는 스킴만 대소문자 구분 없이 필터링해야 한다") {
        runTest {
            viewModel.filteredSchemes.test {
                awaitItem() shouldBe emptyList()
                advanceUntilIdle()
                awaitItem() shouldBe schemes

                viewModel.updateSearchQuery("MAR")
                awaitItem() shouldBe listOf(
                    "market://details?id=com.example.app",
                    "geo:0,0?q=Market",
                )
            }
        }
    }
})
