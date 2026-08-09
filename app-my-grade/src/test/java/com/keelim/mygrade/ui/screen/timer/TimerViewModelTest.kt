package com.keelim.mygrade.ui.screen.timer

import app.cash.turbine.test
import com.keelim.common.extensions.toUiAlignedTwoDigits
import com.keelim.data.repository.HistoryRepository
import com.keelim.data.repository.StudyAnalyticsRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.runCurrent

@OptIn(ExperimentalCoroutinesApi::class)
class TimerViewModelTest : FunSpec({
    lateinit var viewModel: TimerViewModel
    lateinit var studyAnalyticsRepository: StudyAnalyticsRepository
    lateinit var historyRepository: HistoryRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        studyAnalyticsRepository = mockk(relaxed = true)
        historyRepository = mockk(relaxed = true)
        viewModel = TimerViewModel(studyAnalyticsRepository, historyRepository)
    }

    test("getTotalTimeInSeconds calculates correct total from hours minutes and seconds") {
        viewModel.hour = 1
        viewModel.minute = 30
        viewModel.second = 45

        val total = viewModel.getTotalTimeInSeconds()

        total shouldBe 5445
    }

    test("getTotalTimeInSeconds returns zero when no time is set") {
        viewModel.hour = 0
        viewModel.minute = 0
        viewModel.second = 0

        val total = viewModel.getTotalTimeInSeconds()

        total shouldBe 0
    }

    test("start sets timer to running state when time is greater than zero") {
        runTest(testDispatcher) {
            viewModel.hour = 0
            viewModel.minute = 1
            viewModel.second = 0

            viewModel.start()

            viewModel.isRunning shouldBe RunningState.STARTED
            viewModel.leftTime shouldBe 60
        }
    }

    test("start shows unset dialog when time is zero") {
        runTest(testDispatcher) {
            viewModel.hour = 0
            viewModel.minute = 0
            viewModel.second = 0

            viewModel.timerUiState.test {
                viewModel.start()
                advanceUntilIdle()

                awaitItem()
                val state = awaitItem()
                state.isUnsetDialog shouldBe true
            }
        }
    }

    test("stop cancels timer and sets state to stopped") {
        runTest(testDispatcher) {
            viewModel.hour = 0
            viewModel.minute = 1
            viewModel.second = 0

            viewModel.start()
            viewModel.stop()

            viewModel.isRunning shouldBe RunningState.STOPPED
        }
    }

    test("timer decrements leftTime correctly") {
        runTest(testDispatcher) {
            viewModel.hour = 0
            viewModel.minute = 0
            viewModel.second = 3

            viewModel.start()
            advanceTimeBy(1000L)
            runCurrent()

            viewModel.leftTime shouldBe 2
        }
    }

    test("onTimerComplete records session and creates history") {
        runTest(testDispatcher) {
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
    }

    test("clear resets all time values to zero") {
        viewModel.hour = 1
        viewModel.minute = 30
        viewModel.second = 45
        viewModel.start()

        viewModel.clear()

        viewModel.hour shouldBe 0
        viewModel.minute shouldBe 0
        viewModel.second shouldBe 0
        viewModel.leftTime shouldBe 0
    }

    test("clearDialog sets isUnsetDialog to false") {
        runTest(testDispatcher) {
            viewModel.hour = 0
            viewModel.minute = 0
            viewModel.second = 0
            viewModel.start()

            advanceUntilIdle()

            viewModel.clearDialog()

            viewModel.timerUiState.test {
                val state = awaitItem()
                state.isUnsetDialog shouldBe false
            }
        }
    }

    test("formatTime adds leading zero when needed") {
        val result = 5.toUiAlignedTwoDigits(isLeadingZeroNeeded = true)
        result shouldBe "05"
    }

    test("formatTime does not add leading zero when not needed") {
        val result = 5.toUiAlignedTwoDigits(isLeadingZeroNeeded = false)
        result shouldBe " 5"
    }
})
