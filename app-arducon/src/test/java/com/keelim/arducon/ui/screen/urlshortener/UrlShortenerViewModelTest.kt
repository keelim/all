package com.keelim.arducon.ui.screen.urlshortener

import app.cash.turbine.test
import com.keelim.core.data.repository.ShortenedUrlRepository
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class UrlShortenerViewModelTest : FunSpec({

    lateinit var repository: ShortenedUrlRepository
    lateinit var shortenedUrlsFlow: MutableStateFlow<List<ShortenedUrlEntity>>
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)
    val oneDayMillis = 24 * 60 * 60 * 1000L

    extension(mainDispatcherRule)

    fun createViewModel(): UrlShortenerViewModel = UrlShortenerViewModel(repository)

    beforeTest {
        repository = mockk(relaxed = true)
        shortenedUrlsFlow = MutableStateFlow(emptyList())

        every { repository.getAll() } returns shortenedUrlsFlow
        every { repository.getMostClicked(any()) } returns MutableStateFlow(emptyList())
        coEvery { repository.getById(any()) } returns null
        coEvery { repository.getByShortCode(any()) } returns null
        coEvery { repository.insert(any()) } returns 1L
        coEvery { repository.update(any()) } returns Unit
        coEvery { repository.delete(any()) } returns Unit
        coEvery { repository.incrementClickCount(any(), any()) } returns Unit
        coEvery { repository.deleteExpired(any()) } returns Unit
    }

    test("초기화 시 만료된 링크를 정리해야 한다") {
        runTest {
            createViewModel()

            advanceUntilIdle()

            coVerify(exactly = 1) { repository.deleteExpired(any()) }
        }
    }

    test("shortenedUrls는 저장소 목록을 그대로 노출해야 한다") {
        runTest {
            val savedItems = listOf(
                ShortenedUrlEntity(
                    id = 1L,
                    originalUrl = "https://example.com",
                    shortCode = "abc123",
                    title = "example.com",
                ),
            )
            shortenedUrlsFlow.value = savedItems
            val viewModel = createViewModel()
            advanceUntilIdle()

            viewModel.shortenedUrls.test {
                awaitItem() shouldBe emptyList()
                awaitItem() shouldBe savedItems
            }
        }
    }

    test("빈 URL로 생성하면 오류 메시지를 노출하고 저장하지 않아야 한다") {
        runTest {
            val viewModel = createViewModel()

            viewModel.generateShortUrl()
            advanceUntilIdle()

            viewModel.uiState.value.errorMessage shouldBe "URL을 입력해주세요."
            viewModel.uiState.value.isLoading shouldBe false
            coVerify(exactly = 0) { repository.insert(any()) }
        }
    }

    test("유효하지 않은 URL이면 오류 메시지를 노출하고 저장하지 않아야 한다") {
        runTest {
            val viewModel = createViewModel()
            viewModel.updateInputUrl("invalid-url")

            viewModel.generateShortUrl()
            advanceUntilIdle()

            viewModel.uiState.value.errorMessage shouldBe "유효한 URL 형식이 아닙니다."
            viewModel.uiState.value.isLoading shouldBe false
            coVerify(exactly = 0) { repository.insert(any()) }
        }
    }

    test("URL을 다시 입력하면 기존 오류 메시지를 지워야 한다") {
        runTest {
            val viewModel = createViewModel()
            viewModel.generateShortUrl()

            viewModel.updateInputUrl("https://example.com")

            viewModel.uiState.value.inputUrl shouldBe "https://example.com"
            viewModel.uiState.value.errorMessage shouldBe null
        }
    }

    test("유효한 URL 생성 시 기본 제목과 만료일을 저장하고 입력 상태를 초기화해야 한다") {
        runTest {
            val insertedEntity = slot<ShortenedUrlEntity>()
            coEvery { repository.insert(capture(insertedEntity)) } returns 7L
            val viewModel = createViewModel()
            viewModel.updateInputUrl("  https://example.com/path?q=1  ")
            viewModel.updateExpirationDays(7)

            viewModel.generateShortUrl()
            advanceUntilIdle()

            val savedItem = insertedEntity.captured
            savedItem.originalUrl shouldBe "https://example.com/path?q=1"
            savedItem.title shouldBe "example.com"
            savedItem.shortCode.length shouldBe 6
            (savedItem.expiresAt - savedItem.createdAt) shouldBe 7 * oneDayMillis

            viewModel.uiState.value.generatedShortCode shouldBe savedItem.shortCode
            viewModel.uiState.value.inputUrl shouldBe ""
            viewModel.uiState.value.inputTitle shouldBe ""
            viewModel.uiState.value.expirationDays shouldBe 0
            viewModel.uiState.value.isLoading shouldBe false
            viewModel.uiState.value.errorMessage shouldBe null
        }
    }

    test("제목을 직접 입력하면 만료일 없이 그대로 저장해야 한다") {
        runTest {
            val insertedEntity = slot<ShortenedUrlEntity>()
            coEvery { repository.insert(capture(insertedEntity)) } returns 9L
            val viewModel = createViewModel()
            viewModel.updateInputUrl("https://example.com")
            viewModel.updateInputTitle("Custom Title")

            viewModel.generateShortUrl()
            advanceUntilIdle()

            insertedEntity.captured.title shouldBe "Custom Title"
            insertedEntity.captured.expiresAt shouldBe 0L
        }
    }

    test("생성 중 저장소 오류가 나면 오류 메시지를 보여주고 로딩을 종료해야 한다") {
        runTest {
            coEvery { repository.insert(any()) } throws IllegalStateException("db down")
            val viewModel = createViewModel()
            viewModel.updateInputUrl("https://example.com")

            viewModel.generateShortUrl()
            advanceUntilIdle()

            viewModel.uiState.value.errorMessage shouldBe "생성 중 오류가 발생했습니다: db down"
            viewModel.uiState.value.isLoading shouldBe false
            viewModel.uiState.value.inputUrl shouldBe "https://example.com"
            viewModel.uiState.value.generatedShortCode shouldBe ""
        }
    }

    test("recordClick은 클릭 수 증가를 저장소에 위임해야 한다") {
        runTest {
            val viewModel = createViewModel()
            val item = ShortenedUrlEntity(
                id = 42L,
                originalUrl = "https://example.com",
                shortCode = "abc123",
            )

            viewModel.recordClick(item)
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.incrementClickCount(42L, any()) }
        }
    }

    test("deleteItem은 항목 삭제를 저장소에 위임해야 한다") {
        runTest {
            val viewModel = createViewModel()
            val item = ShortenedUrlEntity(
                id = 3L,
                originalUrl = "https://example.com/delete",
                shortCode = "delete",
            )

            viewModel.deleteItem(item)
            advanceUntilIdle()

            coVerify(exactly = 1) { repository.delete(item) }
        }
    }

    test("clearGeneratedCode는 마지막 생성 코드를 지워야 한다") {
        runTest {
            val viewModel = createViewModel()
            viewModel.updateInputUrl("https://example.com")
            viewModel.generateShortUrl()
            advanceUntilIdle()

            viewModel.clearGeneratedCode()

            viewModel.uiState.value.generatedShortCode shouldBe ""
        }
    }
})
