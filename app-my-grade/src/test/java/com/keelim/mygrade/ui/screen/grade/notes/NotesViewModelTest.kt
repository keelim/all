package com.keelim.mygrade.ui.screen.grade.notes

import app.cash.turbine.test
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.model.Notices
import com.keelim.mygrade.testutil.FakeNoteRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest : FunSpec({
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    test("notesUiState exposes repository notes and deleteNote delegates") {
        runTest(testDispatcher) {
            val note = Notices(uid = 1, title = "Math", note = "Solve practice exam")
            val repository = FakeNoteRepository(
                initialNotes = Result.success(listOf(note)),
            )
            val viewModel = NotesViewModel(repository)

            viewModel.notesUiState.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()
                awaitItem() shouldBe SealedUiState.Success(listOf(note))
            }

            viewModel.deleteNote(note)
            advanceUntilIdle()

            repository.deletedNotes shouldBe listOf(note)
        }
    }

    test("repository failure is exposed and retry recollects notes") {
        runTest(testDispatcher) {
            val failure = IllegalStateException("db down")
            val note = Notices(uid = 2, title = "Physics", note = "Review chapter four")
            val repository = FakeNoteRepository(initialNotes = Result.failure(failure))
            val viewModel = NotesViewModel(repository)

            viewModel.notesUiState.test {
                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()

                val error = awaitItem() as SealedUiState.Error
                error.throwable shouldBe failure

                repository.noteListFlow.value = Result.success(listOf(note))
                viewModel.retry()

                awaitItem() shouldBe SealedUiState.Loading
                advanceUntilIdle()
                awaitItem() shouldBe SealedUiState.Success(listOf(note))
            }
        }
    }
})
