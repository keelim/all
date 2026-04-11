package com.keelim.mygrade.ui.screen.grade.edit

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.mygrade.testutil.FakeNoteRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class EditViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("updateNote success updates the dialog state and saved description") {
        runTest(testDispatcher) {
            val repository = FakeNoteRepository()
            val viewModel = EditViewModel(
                savedStateHandle = SavedStateHandle(mapOf("subject" to "Physics")),
                noteRepository = repository,
            )

            viewModel.data.test {
                awaitItem() shouldBe SealedUiState.Loading

                val initial = awaitItem() as SealedUiState.Success
                initial.value shouldBe EditUiState(
                    editResult = EditResult(subject = "Physics"),
                    descriptions = "",
                )

                viewModel.updateNote("Review chapter 4")
                advanceUntilIdle()

                val updated = awaitItem() as SealedUiState.Success
                updated.value shouldBe EditUiState(
                    editResult = EditResult(subject = "Physics"),
                    descriptions = "Review chapter 4",
                    dialogState = EditDialogState.Success,
                )

                viewModel.clearDialogState()

                val cleared = awaitItem() as SealedUiState.Success
                cleared.value.dialogState shouldBe EditDialogState.IDLE
            }

            repository.updatedNotes.single().title shouldBe "Physics"
            repository.updatedNotes.single().note shouldBe "Review chapter 4"
        }
    }

    test("updateNote failure exposes a failed dialog state") {
        runTest(testDispatcher) {
            val repository = FakeNoteRepository(
                updateResult = Result.failure(IllegalStateException("db down")),
            )
            val viewModel = EditViewModel(
                savedStateHandle = SavedStateHandle(mapOf("subject" to "Chemistry")),
                noteRepository = repository,
            )

            viewModel.data.test {
                awaitItem() shouldBe SealedUiState.Loading
                awaitItem()

                viewModel.updateNote("Memorize equations")
                advanceUntilIdle()

                val updated = awaitItem() as SealedUiState.Success
                updated.value shouldBe EditUiState(
                    editResult = EditResult(subject = "Chemistry"),
                    descriptions = "Memorize equations",
                    dialogState = EditDialogState.Failed,
                )
            }
        }
    }
})
