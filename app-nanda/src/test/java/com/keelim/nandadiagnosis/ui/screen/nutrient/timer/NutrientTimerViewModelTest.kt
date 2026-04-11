package com.keelim.nandadiagnosis.ui.screen.nutrient.timer

import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NutrientTimerViewModelTest : FunSpec({
    lateinit var viewModel: NutrientTimerViewModel
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    beforeTest {
        viewModel = NutrientTimerViewModel()
    }

    test("getTotalTimeInSeconds returns the configured total") {
        viewModel.hour = 1
        viewModel.minute = 2
        viewModel.second = 3

        viewModel.getTotalTimeInSeconds() shouldBe 3723
    }

    test("start does nothing when total time is zero") {
        runTest(testDispatcher) {
            viewModel.start()

            viewModel.isRunning shouldBe RunningState.STOPPED
            viewModel.leftTime.intValue shouldBe 0
        }
    }

    test("start sets running state and initializes leftTime") {
        runTest(testDispatcher) {
            viewModel.minute = 1

            viewModel.start()

            viewModel.isRunning shouldBe RunningState.STARTED
            viewModel.leftTime.intValue shouldBe 60
        }
    }

    test("timer ticks down over time") {
        runTest(testDispatcher) {
            viewModel.second = 3

            viewModel.start()
            advanceTimeBy(1_000L)
            runCurrent()

            viewModel.leftTime.intValue shouldBe 2
        }
    }

    test("timer can count down to zero") {
        runTest(testDispatcher) {
            viewModel.second = 2

            viewModel.start()
            advanceUntilIdle()

            viewModel.leftTime.intValue shouldBe 0
        }
    }

    test("stop cancels the timer and resets running state") {
        runTest(testDispatcher) {
            viewModel.second = 5
            viewModel.start()

            viewModel.stop()

            viewModel.isRunning shouldBe RunningState.STOPPED
        }
    }

    test("addTime returns a formatted clock string") {
        viewModel.hour = 1
        viewModel.minute = 2
        viewModel.second = 3

        val result = viewModel.addTime(System.currentTimeMillis())

        Regex("""\d{2}:\d{2}:\d{2} (AM|PM)""").matches(result) shouldBe true
    }

    test("formatTime and picker lists follow two-digit UI rules") {
        formatTime(isLeadingZeroNeeded = true, value = 5) shouldBe "05"
        formatTime(isLeadingZeroNeeded = false, value = 5) shouldBe " 5"
        HOUR_LIST.first() shouldBe 0
        HOUR_LIST.last() shouldBe 12
        MINUTE_LIST.last() shouldBe 60
        SECOND_LIST.last() shouldBe 60
    }
})
