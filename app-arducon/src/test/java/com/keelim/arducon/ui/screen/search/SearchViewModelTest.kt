package com.keelim.arducon.ui.screen.search

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.keelim.data.repository.ArduconRepository
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
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var mockRepository: ArduconRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()

        // ViewModel 생성자에서 호출되는 메서드 모킹
        every { mockRepository.getSchemeList() } returns flowOf(emptyList())

        viewModel = SearchViewModel(mockRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `검색어 지우기 시 빈 문자열로 초기화되어야 한다`() = runTest {
        viewModel.updateSearchQuery("test")
        viewModel.clearSearch()

        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEqualTo("")
        }
    }

    @Test
    fun `검색어 업데이트가 정상적으로 작동해야 한다`() = runTest {
        viewModel.updateSearchQuery("test")

        viewModel.searchQuery.test {
            assertThat(awaitItem()).isEqualTo("test")
        }
    }
}
