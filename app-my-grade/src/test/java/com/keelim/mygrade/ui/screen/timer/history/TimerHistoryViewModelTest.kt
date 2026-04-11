package com.keelim.mygrade.ui.screen.timer.history

import app.cash.turbine.test
import com.keelim.model.TimerHistoryModel
import com.keelim.mygrade.testutil.FakeHistoryRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class TimerHistoryViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("uiState exposes timer histories as loaded content") {
        runTest(testDispatcher) {
            val history = TimerHistoryModel(
                uid = 1,
                date = "2026-03-08T10:00:00",
                hours = 1,
                minutes = 20,
                seconds = 30,
                description = "Morning study",
                isCompleted = false,
            )
            val repository = FakeHistoryRepository(timerHistories = listOf(history))
            val viewModel = TimerHistoryViewModel(repository)

            viewModel.uiState.test {
                awaitItem() shouldBe TimerHistoryUiState()
                advanceUntilIdle()
                awaitItem() shouldBe TimerHistoryUiState(
                    histories = listOf(history),
                    isLoading = false,
                )
            }
        }
    }

    test("delete and update actions delegate to the history repository") {
        runTest(testDispatcher) {
            val repository = FakeHistoryRepository()
            val viewModel = TimerHistoryViewModel(repository)

            viewModel.deleteHistory(historyId = 7)
            viewModel.updateDescription(historyId = 7, description = "Updated note")
            viewModel.deleteAll()
            advanceUntilIdle()

            repository.deletedTimerHistoryIds shouldBe listOf(7)
            repository.updatedDescriptions shouldBe listOf(7 to "Updated note")
            repository.deleteAllTimerHistoriesCallCount shouldBe 1
        }
    }
})
