package com.keelim.mygrade.ui.screen.grade

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.keelim.data.repository.HistoryRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class GradeViewModelTest : FunSpec({
    lateinit var viewModel: GradeViewModel
    lateinit var savedStateHandle: SavedStateHandle
    lateinit var historyRepository: HistoryRepository
    val testDispatcher = StandardTestDispatcher()

    beforeTest {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(
            mapOf(
                "subject" to "Math",
                "grade" to "A",
                "point" to "4.3",
            )
        )
        historyRepository = mockk(relaxed = true)
    }

    afterTest {
        Dispatchers.resetMain()
    }

    test("initial state should be empty") {
        runTest(testDispatcher) {
            coEvery { historyRepository.create(any(), any(), any()) } returns false
            viewModel = GradeViewModel(savedStateHandle, historyRepository)

            viewModel.uiState.test {
                val state = awaitItem()
                state.isMessageShow shouldBe false
                state.message shouldBe ""
            }
        }
    }

    test("updateMessage creates history and shows success message") {
        runTest(testDispatcher) {
            coEvery { historyRepository.create(any(), any(), any()) } returns true
            viewModel = GradeViewModel(savedStateHandle, historyRepository)

            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                state.isMessageShow shouldBe true
                state.message shouldBe "처리 완료되었습니다. "
            }

            coVerify { historyRepository.create("Math", any(), any()) }
        }
    }

    test("updateMessage does not show message when history creation fails") {
        runTest(testDispatcher) {
            coEvery { historyRepository.create(any(), any(), any()) } returns false
            viewModel = GradeViewModel(savedStateHandle, historyRepository)

            advanceUntilIdle()

            viewModel.uiState.test {
                val state = awaitItem()
                state.isMessageShow shouldBe false
                state.message shouldBe ""
            }
        }
    }

    test("dismissMessage hides message and clears text") {
        runTest(testDispatcher) {
            coEvery { historyRepository.create(any(), any(), any()) } returns true
            viewModel = GradeViewModel(savedStateHandle, historyRepository)

            advanceUntilIdle()

            viewModel.dismissMessage()

            viewModel.uiState.test {
                val state = awaitItem()
                state.isMessageShow shouldBe false
                state.message shouldBe ""
            }
        }
    }

    test("data flow contains GradeResult from savedStateHandle") {
        runTest(testDispatcher) {
            coEvery { historyRepository.create(any(), any(), any()) } returns false
            viewModel = GradeViewModel(savedStateHandle, historyRepository)

            viewModel.data.test {
                val gradeResult = awaitItem()
                gradeResult.subject shouldBe "Math"
            }
        }
    }
})
