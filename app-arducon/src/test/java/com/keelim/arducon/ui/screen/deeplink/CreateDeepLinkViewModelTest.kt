package com.keelim.arducon.ui.screen.deeplink

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.keelim.data.repository.ArduconRepository
import io.kotest.core.spec.style.FunSpec
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDeepLinkViewModelTest : FunSpec({

    lateinit var viewModel: CreateDeepLinkViewModel
    lateinit var mockRepository: ArduconRepository
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()

        every { mockRepository.getCategories() } returns flowOf(emptyList())

        viewModel = CreateDeepLinkViewModel(mockRepository, testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("URL 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateUrl("https://example.com")

            viewModel.url.test {
                assertThat(awaitItem()).isEqualTo("https://example.com")
            }
        }
    }

    test("제목 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateTitle("테스트 딥링크")

            viewModel.title.test {
                assertThat(awaitItem()).isEqualTo("테스트 딥링크")
            }
        }
    }

    test("카테고리 업데이트가 정상적으로 작동해야 한다") {
        runTest {
            viewModel.updateCategory("웹사이트")

            viewModel.category.test {
                assertThat(awaitItem()).isEqualTo("웹사이트")
            }
        }
    }

    test("전체 URL 가져오기가 정상적으로 작동해야 한다") {
        viewModel.updateUrl("https://example.com")
        assertThat(viewModel.getFullUrl()).isEqualTo("https://example.com")
    }

    test("유효한 URL 검증이 정상적으로 작동해야 한다") {
        viewModel.updateUrl("https://example.com")
        assertThat(viewModel.isValidUrl()).isTrue()
    }

    test("유효하지 않은 URL 검증이 정상적으로 작동해야 한다") {
        viewModel.updateUrl("invalid-url")
        assertThat(viewModel.isValidUrl()).isFalse()
    }
})
