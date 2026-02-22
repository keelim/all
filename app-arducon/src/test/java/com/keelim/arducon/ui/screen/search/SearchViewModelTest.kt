package com.keelim.arducon.ui.screen.search

import app.cash.turbine.test
import com.keelim.data.repository.ArduconRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
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
class SearchViewModelTest : FunSpec({

    lateinit var viewModel: SearchViewModel
    lateinit var mockRepository: ArduconRepository
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()

        every { mockRepository.getSchemeList() } returns flowOf(emptyList())

        viewModel = SearchViewModel(mockRepository, testDispatcher)
    }

    afterTest {
        Dispatchers.resetMain()
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
})
