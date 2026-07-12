package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.keelim.data.repository.NandaRepository
import com.keelim.model.NandaDiagnosis
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class DiagnosisViewModelTest : FunSpec({
    lateinit var repository: NandaRepository
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    fun createViewModel(
        num: String?,
        diagnoses: Flow<List<NandaDiagnosis>>,
    ): DiagnosisViewModel {
        repository = mockk()
        every { repository.nandaDiagnosis } returns diagnoses
        return DiagnosisViewModel(
            savedStateHandle = SavedStateHandle(mapOf("num" to num)),
            nandaRepository = repository,
        )
    }

    test("blank query exposes all diagnoses in the selected category") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                num = "1",
                diagnoses = MutableStateFlow(buildDiagnoses(224)),
            )

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()

                val state = awaitItem() as DiagnosisScreenState.Success
                state.items.size shouldBe 12
                state.items.first().diagnosis shouldBe "Diagnosis 0"
                state.items.last().diagnosis shouldBe "Diagnosis 11"

                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("search filters diagnoses ignoring case and returns empty when nothing matches") {
        runTest(testDispatcher) {
            val diagnoses = MutableStateFlow(
                listOf(
                    diagnosis("Acute Pain"),
                    diagnosis("Chronic pain"),
                    diagnosis("Activity intolerance"),
                    diagnosis("Ineffective airway clearance"),
                    diagnosis("Risk for falls"),
                    diagnosis("Impaired comfort"),
                    diagnosis("Sleep deprivation"),
                    diagnosis("Anxiety"),
                    diagnosis("Fatigue"),
                    diagnosis("Nausea"),
                    diagnosis("Hyperthermia"),
                    diagnosis("Hypothermia"),
                ),
            )
            val viewModel = createViewModel(num = "1", diagnoses = diagnoses)

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()

                val initial = awaitItem() as DiagnosisScreenState.Success
                initial.items.size shouldBe 12

                viewModel.search("PAIN")
                advanceUntilIdle()

                val filtered = awaitItem() as DiagnosisScreenState.Success
                filtered.items.map { it.diagnosis } shouldBe listOf("Acute Pain", "Chronic pain")

                viewModel.search("not-found")
                advanceUntilIdle()

                awaitItem() shouldBe DiagnosisScreenState.Empty
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("invalid category emits empty state") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                num = "99",
                diagnoses = MutableStateFlow(buildDiagnoses(30)),
            )

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()

                awaitItem() shouldBe DiagnosisScreenState.Empty
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("category range beyond repository size emits empty state") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                num = "13",
                diagnoses = MutableStateFlow(buildDiagnoses(200)),
            )

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()

                awaitItem() shouldBe DiagnosisScreenState.Empty
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("repository failure emits error state") {
        runTest(testDispatcher) {
            val viewModel = createViewModel(
                num = "1",
                diagnoses = flow { throw IllegalStateException("boom") },
            )

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()

                awaitItem() shouldBe DiagnosisScreenState.Error
                cancelAndIgnoreRemainingEvents()
            }
        }
    }

    test("retry resubscribes after a repository failure") {
        runTest(testDispatcher) {
            var subscriptionCount = 0
            val viewModel = createViewModel(
                num = "1",
                diagnoses = flow {
                    if (subscriptionCount++ == 0) {
                        throw IllegalStateException("boom")
                    }
                    emit(buildDiagnoses(224))
                },
            )

            viewModel.screenState.test {
                awaitItem() shouldBe DiagnosisScreenState.Loading
                advanceUntilIdle()
                awaitItem() shouldBe DiagnosisScreenState.Error

                viewModel.retry()
                advanceUntilIdle()

                awaitItem() shouldBe DiagnosisScreenState.Loading
                val recovered = awaitItem() as DiagnosisScreenState.Success
                recovered.items.size shouldBe 12
                subscriptionCount shouldBe 2

                cancelAndIgnoreRemainingEvents()
            }
        }
    }
}) {
    companion object {
        private fun diagnosis(reason: String) = NandaDiagnosis(
            reason = reason,
            domain = "domain",
            className = "class",
            definition = "definition",
        )

        private fun buildDiagnoses(count: Int): List<NandaDiagnosis> =
            List(count) { index -> diagnosis("Diagnosis $index") }
    }
}
