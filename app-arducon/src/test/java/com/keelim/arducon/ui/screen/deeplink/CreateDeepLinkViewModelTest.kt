package com.keelim.arducon.ui.screen.deeplink

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
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDeepLinkViewModelTest : FunSpec({

    lateinit var viewModel: CreateDeepLinkViewModel
    lateinit var mockRepository: ArduconRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        mockRepository = mockk()

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
})
