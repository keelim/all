package com.keelim.mygrade.ui.screen.task

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
class TaskViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("state groups tasks into todo and completed sections") {
        runTest(testDispatcher) {
            val todoTask = LocalTask(
                id = "1",
                title = "Read",
                description = "Read chapter one",
                isCompleted = false,
                date = "2026-03-08",
                isEditing = false,
            )
            val completedTask = LocalTask(
                id = "2",
                title = "Review",
                description = "Review notes",
                isCompleted = true,
                date = "2026-03-08",
                isEditing = false,
            )
            val repository = FakeDefaultTaskRepository(
                initialTasks = listOf(todoTask, completedTask),
            )
            val viewModel = TaskViewModel(repository)

            viewModel.state.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()

                val success = awaitItem() as SealedUiState.Success
                success.value shouldBe listOf(
                    TaskElement.Header(text = "Todo"),
                    TaskElement.Item(localTask = todoTask, role = TaskElement.Role.SINGLE),
                    TaskElement.Header(text = "완료"),
                    TaskElement.Item(localTask = completedTask, role = TaskElement.Role.SINGLE),
                )
            }
        }
    }

    test("task actions delegate to the repository") {
        runTest(testDispatcher) {
            val task = LocalTask(
                id = "3",
                title = "Practice",
                description = "Solve exercises",
                isCompleted = false,
                date = "2026-03-08",
                isEditing = false,
            )
            val repository = FakeDefaultTaskRepository()
            val viewModel = TaskViewModel(repository)

            viewModel.addLocalTask()
            viewModel.editTask(task)
            viewModel.deleteTask(task)
            viewModel.clear()
            advanceUntilIdle()

            repository.createCallCount shouldBe 1
            repository.upsertedTasks shouldBe listOf(task)
            repository.deletedTasks shouldBe listOf(task)
            repository.clearCallCount shouldBe 1
        }
    }
})
