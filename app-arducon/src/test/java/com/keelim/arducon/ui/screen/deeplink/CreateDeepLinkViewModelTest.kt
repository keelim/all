package com.keelim.arducon.ui.screen.deeplink

import app.cash.turbine.test
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDeepLinkViewModelTest : FunSpec({

    lateinit var viewModel: CreateDeepLinkViewModel
    lateinit var mockRepository: ArduconRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        mockRepository = mockk(relaxed = true)

        every { mockRepository.getCategories() } returns flowOf(emptyList())

        viewModel = CreateDeepLinkViewModel(mockRepository, testDispatcher)
    }

    test("URL 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateUrl("https://example.com")

            viewModel.url.test {
                awaitItem() shouldBe "https://example.com"
            }
        }
    }

    test("제목 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateTitle("테스트 딥링크")

            viewModel.title.test {
                awaitItem() shouldBe "테스트 딥링크"
            }
        }
    }

    test("카테고리 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateCategory("웹사이트")

            viewModel.category.test {
                awaitItem() shouldBe "웹사이트"
            }
        }
    }

    test("전체 URL 가져오기가 정상적으로 작동해야 한다") {
        viewModel.updateUrl("https://example.com")
        viewModel.getFullUrl() shouldBe "https://example.com"
    }

    test("유효한 URL 검증이 정상적으로 작동해야 한다") {
        viewModel.updateUrl("https://example.com")
        viewModel.isValidUrl() shouldBe true
    }

    test("유효하지 않은 URL 검증이 정상적으로 작동해야 한다") {
        viewModel.updateUrl("invalid-url")
        viewModel.isValidUrl() shouldBe false
    }

    test("스킴 설정 시 URL이 비어 있으면 기본 형식을 채워야 한다") {
        viewModel.setScheme("example")

        viewModel.scheme.value shouldBe "example"
        viewModel.url.value shouldBe "example://"
    }

    test("스킴 설정 시 기존 URL은 유지해야 한다") {
        viewModel.updateUrl("https://existing.example.com")

        viewModel.setScheme("example")

        viewModel.scheme.value shouldBe "example"
        viewModel.url.value shouldBe "https://existing.example.com"
    }

    test("빈 URL로 생성하면 저장소에 저장하지 않아야 한다") {
        runTest {
            viewModel.createDeepLink()
            advanceUntilIdle()

            viewModel.isLoading.value shouldBe false
            viewModel.isSuccess.value shouldBe false
            coVerify(exactly = 0) { mockRepository.insertDeepLinkUrl(any()) }
        }
    }

    test("유효한 URL 생성 시 딥링크를 저장하고 입력 상태를 초기화해야 한다") {
        runTest {
            val deepLinkSlot = slot<DeepLink>()
            coEvery { mockRepository.insertDeepLinkUrl(capture(deepLinkSlot)) } returns Unit
            viewModel.updateUrl("https://example.com/path")
            viewModel.updateTitle("테스트 딥링크")
            viewModel.updateCategory("웹사이트")

            viewModel.createDeepLink()
            advanceUntilIdle()

            deepLinkSlot.captured.url shouldBe "https://example.com/path"
            deepLinkSlot.captured.title shouldBe "테스트 딥링크"
            deepLinkSlot.captured.category shouldBe "웹사이트"
            viewModel.isSuccess.value shouldBe true
            viewModel.isLoading.value shouldBe false
            viewModel.url.value shouldBe ""
            viewModel.title.value shouldBe ""
            viewModel.category.value shouldBe ""
        }
    }

    test("resetSuccess는 성공 상태를 다시 false로 돌려야 한다") {
        runTest {
            coEvery { mockRepository.insertDeepLinkUrl(any()) } returns Unit
            viewModel.updateUrl("https://example.com/path")

            viewModel.createDeepLink()
            advanceUntilIdle()
            viewModel.resetSuccess()

            viewModel.isSuccess.value shouldBe false
        }
    }
})
