package com.keelim.nandadiagnosis.wellness

import com.keelim.data.repository.WellnessRepository
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessGoal
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import com.keelim.testing.platform.FakeTimeProvider
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class WellnessViewModelTest : FunSpec({
    val dispatcher = UnconfinedTestDispatcher()

    beforeTest { kotlinx.coroutines.Dispatchers.setMain(dispatcher) }

    afterTest { kotlinx.coroutines.Dispatchers.resetMain() }

    test("valid writes persist and invalid inputs do not write") {
        runTest {
            val repository = FakeWellnessRepository()
    val viewModel = WellnessViewModel(
        repository = repository,
        timeProvider = FakeTimeProvider(
            initialInstant = Instant.parse("2026-07-19T00:00:00Z"),
            zone = ZoneId.of("UTC"),
        ),
    )
            val date = LocalDate.of(2026, 7, 19)

            viewModel.saveMeasurement("12.3", "9,5", MeasurementState.RELAXED, date)
            viewModel.saveMeasurement("13.0", "10.0", MeasurementState.STRETCHED, date)
            viewModel.saveMeasurement("12.34", "9.5", MeasurementState.MAXIMUM, date.plusDays(1))
            viewModel.saveMeasurement("14.0", "11.0", MeasurementState.MAXIMUM, date.plusDays(1))
            advanceUntilIdle()

            repository.state.value.measurements shouldBe
                listOf(
                    Measurement(date.toString(), 13.0, 10.0, MeasurementState.STRETCHED.name),
                    Measurement(
                        date.plusDays(1).toString(),
                        14.0,
                        11.0,
                        MeasurementState.MAXIMUM.name,
                    ),
                )

            viewModel.addRoutine("   ", RoutineKind.RUNNING, date)
            viewModel.addRoutine("  Run  ", RoutineKind.RUNNING, date)
            advanceUntilIdle()

            val running = repository.state.value.routines.single()
            viewModel.setRoutineCompletion(running, date, checked = true, duration = 0)
            advanceUntilIdle()

            repository.state.value.completions shouldBe emptyList()
            viewModel.uiState.value.validationErrors shouldBe
                setOf(WellnessValidationError.DURATION)

            viewModel.setRoutineCompletion(running, date, checked = true, duration = 25)
            advanceUntilIdle()

            repository.state.value.routines.single().name shouldBe "Run"
            repository.state.value.completions.single().durationMinutes shouldBe 25

            viewModel.setRoutineCompletion(running, date, checked = false, duration = 0)
            viewModel.addRoutine("Supplement", RoutineKind.SUPPLEMENT, date)
            advanceUntilIdle()
            val supplement =
                repository.state.value.routines.single {
                    it.kind == RoutineKind.SUPPLEMENT.name
                }
            viewModel.setRoutineCompletion(supplement, date, checked = true, duration = 30)
            advanceUntilIdle()

            repository.state.value.completions.single().durationMinutes shouldBe null
        }
    }

})

private class FakeWellnessRepository : WellnessRepository {
    val state = MutableStateFlow(WellnessData())
    override val data = state
    private var nextRoutineId = 1L

    override suspend fun initializeDefaultRoutines(createdLocalDate: String) = Unit

    override suspend fun upsertMeasurement(measurement: Measurement) {
        state.update {
            it.copy(
                measurements =
                    it.measurements.filterNot { current ->
                        current.localDate == measurement.localDate
                    } + measurement,
            )
        }
    }

    override suspend fun upsertGoal(goal: WellnessGoal) {
        state.update { it.copy(goal = goal) }
    }

    override suspend fun deleteGoal() {
        state.update { it.copy(goal = null) }
    }

    override suspend fun insertRoutine(routine: Routine): Long {
        val id = nextRoutineId++
        state.update { it.copy(routines = it.routines + routine.copy(id = id)) }
        return id
    }

    override suspend fun deleteRoutine(routine: Routine) {
        state.update {
            it.copy(
                routines = it.routines.filterNot { current -> current.id == routine.id },
                completions =
                    it.completions.filterNot { current -> current.routineId == routine.id },
            )
        }
    }

    override suspend fun upsertRoutineCompletion(completion: RoutineCompletion) {
        state.update {
            it.copy(
                completions =
                    it.completions.filterNot { current ->
                        current.routineId == completion.routineId &&
                            current.localDate == completion.localDate
                    } + completion,
            )
        }
    }

    override suspend fun deleteRoutineCompletion(completion: RoutineCompletion) {
        state.update {
            it.copy(
                completions =
                    it.completions.filterNot { current ->
                        current.routineId == completion.routineId &&
                            current.localDate == completion.localDate
                    },
            )
        }
    }
}
