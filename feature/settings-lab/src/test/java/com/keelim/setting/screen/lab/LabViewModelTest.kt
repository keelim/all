package com.keelim.setting.screen.lab

import com.keelim.data.repository.PromptRepository
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class LabViewModelTest : FunSpec({
    extensions(MainDispatcherRule())

    test("queuePrompt moves from loading to success and formats the prompt") {
        runTest {
            val pendingResult = CompletableDeferred<Result<String>>()
            val repository =
                FakePromptRepository(
                    deferredResult = pendingResult,
                )
            val viewModel = LabViewModel(repository)

            viewModel.uiState.value shouldBe LabUiState.Initial

            viewModel.queuePrompt("This text needs a summary.")

            viewModel.uiState.value shouldBe LabUiState.Loading
            repository.lastPrompt shouldBe
                "Summarize the following text for me: This text needs a summary."
            repository.invocationCount shouldBe 1

            pendingResult.complete(Result.success("Short summary"))

            advanceUntilIdle()

            viewModel.uiState.value shouldBe LabUiState.Success("Short summary")
        }
    }

    test("queuePrompt exposes repository failures as an error state") {
        runTest {
            val repository =
                FakePromptRepository(
                    result = Result.failure(IllegalStateException("generation failed")),
                )
            val viewModel = LabViewModel(repository)

            viewModel.queuePrompt("Broken request")
            advanceUntilIdle()

            repository.lastPrompt shouldBe "Summarize the following text for me: Broken request"
            repository.invocationCount shouldBe 1
            viewModel.uiState.value shouldBe LabUiState.Error("generation failed")
        }
    }

    test("queuePrompt rethrows cancellation without replacing loading state") {
        runTest {
            val repository =
                FakePromptRepository(
                    throwable = CancellationException("cancelled"),
                )
            val viewModel = LabViewModel(repository)

            viewModel.queuePrompt("Cancelled request")
            viewModel.uiState.value shouldBe LabUiState.Loading

            advanceUntilIdle()

            repository.lastPrompt shouldBe "Summarize the following text for me: Cancelled request"
            repository.invocationCount shouldBe 1
            viewModel.uiState.value shouldBe LabUiState.Loading
        }
    }
})

private class FakePromptRepository(
    private val result: Result<String> = Result.success(""),
    private val deferredResult: CompletableDeferred<Result<String>>? = null,
    private val throwable: Throwable? = null,
) : PromptRepository {
    var invocationCount: Int = 0
        private set

    var lastPrompt: String? = null
        private set

    override suspend fun getContent(prompt: String): Result<String> {
        invocationCount += 1
        lastPrompt = prompt
        throwable?.let { throw it }
        return deferredResult?.await() ?: result
    }
}
