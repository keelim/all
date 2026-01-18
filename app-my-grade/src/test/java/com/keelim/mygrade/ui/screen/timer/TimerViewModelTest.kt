package com.keelim.mygrade.ui.screen.timer

import app.cash.turbine.test
import com.keelim.data.repository.HistoryRepository
import com.keelim.data.repository.StudyAnalyticsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class TimerViewModelTest {
    private lateinit var viewModel: TimerViewModel
    private lateinit var studyAnalyticsRepository: StudyAnalyticsRepository
    private lateinit var historyRepository: HistoryRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        studyAnalyticsRepository = mockk(relaxed = true)
        historyRepository = mockk(relaxed = true)
        viewModel = TimerViewModel(studyAnalyticsRepository, historyRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getTotalTimeInSeconds calculates correct total from hours minutes and seconds`() {
        viewModel.hour = 1
        viewModel.minute = 30
        viewModel.second = 45

        val total = viewModel.getTotalTimeInSeconds()

        assertEquals(5445, total)
    }

    @Test
    fun `getTotalTimeInSeconds returns zero when no time is set`() {
        viewModel.hour = 0
        viewModel.minute = 0
        viewModel.second = 0

        val total = viewModel.getTotalTimeInSeconds()

        assertEquals(0, total)
    }

    @Test
    fun `start sets timer to running state when time is greater than zero`() = runTest {
        viewModel.hour = 0
        viewModel.minute = 1
        viewModel.second = 0

        viewModel.start()

        assertEquals(RunningState.STARTED, viewModel.isRunning)
        assertEquals(60, viewModel.leftTime.intValue)
    }

    @Test
    fun `start shows unset dialog when time is zero`() = runTest {
        viewModel.hour = 0
        viewModel.minute = 0
        viewModel.second = 0

        viewModel.timerUiState.test {
            viewModel.start()
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state.isUnsetDialog)
        }
    }

    @Test
    fun `stop cancels timer and sets state to stopped`() = runTest {
        viewModel.hour = 0
        viewModel.minute = 1
        viewModel.second = 0

        viewModel.start()
        viewModel.stop()

        assertEquals(RunningState.STOPPED, viewModel.isRunning)
    }

    @Test
    fun `timer decrements leftTime correctly`() = runTest {
        viewModel.hour = 0
        viewModel.minute = 0
        viewModel.second = 3

        viewModel.start()
        advanceTimeBy(1000L)

        assertEquals(2, viewModel.leftTime.intValue)
    }

    @Test
    fun `onTimerComplete records session and creates history`() = runTest {
        coEvery { studyAnalyticsRepository.recordSession(any(), any()) } returns Unit
        coEvery { historyRepository.createTimerHistory(any(), any(), any()) } returns Unit

        viewModel.hour = 1
        viewModel.minute = 30
        viewModel.second = 45
        viewModel.start()
        viewModel.onTimerComplete()

        advanceUntilIdle()

        coVerify { studyAnalyticsRepository.recordSession("Default", 5445) }
        coVerify { historyRepository.createTimerHistory(1, 30, 45) }
    }

    @Test
    fun `clear resets all time values to zero`() {
        viewModel.hour = 1
        viewModel.minute = 30
        viewModel.second = 45
        viewModel.leftTime.intValue = 5445

        viewModel.clear()

        assertEquals(0, viewModel.hour)
        assertEquals(0, viewModel.minute)
        assertEquals(0, viewModel.second)
        assertEquals(0, viewModel.leftTime.intValue)
    }

    @Test
    fun `clearDialog sets isUnsetDialog to false`() = runTest {
        viewModel.hour = 0
        viewModel.minute = 0
        viewModel.second = 0
        viewModel.start()

        advanceUntilIdle()

        viewModel.clearDialog()

        viewModel.timerUiState.test {
            val state = awaitItem()
            assertFalse(state.isUnsetDialog)
        }
    }

    @Test
    fun `formatTime adds leading zero when needed`() {
        val result = formatTime(isLeadingZeroNeeded = true, value = 5)
        assertEquals("05", result)
    }

    @Test
    fun `formatTime does not add leading zero when not needed`() {
        val result = formatTime(isLeadingZeroNeeded = false, value = 5)
        assertEquals(" 5", result)
    }
}
