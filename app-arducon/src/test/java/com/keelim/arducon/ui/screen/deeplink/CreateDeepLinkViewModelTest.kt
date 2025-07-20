package com.keelim.arducon.ui.screen.deeplink

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.keelim.data.repository.ArduconRepository
import com.keelim.model.DeepLink
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateDeepLinkViewModelTest {

    private lateinit var viewModel: CreateDeepLinkViewModel
    private lateinit var mockRepository: ArduconRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        
        // ViewModel 생성자에서 호출되는 메서드 모킹
        every { mockRepository.getCategories() } returns flowOf(emptyList())
        
        viewModel = CreateDeepLinkViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `URL 업데이트가 정상적으로 작동해야 한다`() = runTest {
        viewModel.updateUrl("https://example.com")

        viewModel.url.test {
            assertThat(awaitItem()).isEqualTo("https://example.com")
        }
    }

    @Test
    fun `제목 업데이트가 정상적으로 작동해야 한다`() = runTest {
        viewModel.updateTitle("테스트 딥링크")

        viewModel.title.test {
            assertThat(awaitItem()).isEqualTo("테스트 딥링크")
        }
    }

    @Test
    fun `카테고리 업데이트가 정상적으로 작동해야 한다`() = runTest {
        viewModel.updateCategory("웹사이트")

        viewModel.category.test {
            assertThat(awaitItem()).isEqualTo("웹사이트")
        }
    }

    @Test
    fun `전체 URL 가져오기가 정상적으로 작동해야 한다`() {
        viewModel.updateUrl("https://example.com")
        assertThat(viewModel.getFullUrl()).isEqualTo("https://example.com")
    }

    @Test
    fun `유효한 URL 검증이 정상적으로 작동해야 한다`() {
        viewModel.updateUrl("https://example.com")
        assertThat(viewModel.isValidUrl()).isTrue()
    }

    @Test
    fun `유효하지 않은 URL 검증이 정상적으로 작동해야 한다`() {
        viewModel.updateUrl("invalid-url")
        assertThat(viewModel.isValidUrl()).isFalse()
    }
}
