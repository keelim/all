package com.keelim.mygrade.ui.screen.task.chart

import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.model.LocalTask
import com.keelim.mygrade.testutil.FakeDefaultTaskRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class TaskChartViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("state maps tasks into pie chart entries with equal percentages") {
        runTest(testDispatcher) {
            val repository = FakeDefaultTaskRepository(
                initialTasks = listOf(
                    LocalTask(
                        id = "1",
                        title = "Read",
                        description = "Read chapter one",
                        isCompleted = false,
                        date = "2026-03-08",
                        isEditing = false,
                    ),
                    LocalTask(
                        id = "2",
                        title = "Write",
                        description = "Write summary",
                        isCompleted = true,
                        date = "2026-03-08",
                        isEditing = false,
                    ),
                ),
            )
            val viewModel = TaskChartViewModel(repository)

            viewModel.state.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()

                val success = awaitItem() as SealedUiState.Success
                success.value.map { it.name } shouldBe listOf("Read", "Write")
                success.value.map { it.percentage } shouldBe listOf(0.5f, 0.5f)
            }
        }
    }
})
