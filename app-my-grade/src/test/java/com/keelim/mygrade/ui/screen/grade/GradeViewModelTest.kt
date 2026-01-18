package com.keelim.mygrade.ui.screen.grade

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.keelim.data.repository.HistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GradeViewModelTest {
    private lateinit var viewModel: GradeViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var historyRepository: HistoryRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(
            mapOf(
                "subject" to "Math",
                "my" to "85",
                "average" to "75.0",
                "std" to "10.0",
                "total" to "100",
            )
        )
        historyRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() = runTest {
        coEvery { historyRepository.create(any(), any(), any()) } returns false
        viewModel = GradeViewModel(savedStateHandle, historyRepository)

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isMessageShow)
            assertEquals("", state.message)
        }
    }

    @Test
    fun `updateMessage creates history and shows success message`() = runTest {
        coEvery { historyRepository.create(any(), any(), any()) } returns true
        viewModel = GradeViewModel(savedStateHandle, historyRepository)

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.isMessageShow)
            assertEquals("처리 완료되었습니다. ", state.message)
        }

        coVerify { historyRepository.create("Math", any(), any()) }
    }

    @Test
    fun `updateMessage does not show message when history creation fails`() = runTest {
        coEvery { historyRepository.create(any(), any(), any()) } returns false
        viewModel = GradeViewModel(savedStateHandle, historyRepository)

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isMessageShow)
            assertEquals("", state.message)
        }
    }

    @Test
    fun `dismissMessage hides message and clears text`() = runTest {
        coEvery { historyRepository.create(any(), any(), any()) } returns true
        viewModel = GradeViewModel(savedStateHandle, historyRepository)

        advanceUntilIdle()

        viewModel.dismissMessage()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isMessageShow)
            assertEquals("", state.message)
        }
    }

    @Test
    fun `data flow contains GradeResult from savedStateHandle`() = runTest {
        coEvery { historyRepository.create(any(), any(), any()) } returns false
        viewModel = GradeViewModel(savedStateHandle, historyRepository)

        viewModel.data.test {
            val gradeResult = awaitItem()
            assertEquals("Math", gradeResult.subject)
        }
    }
}
