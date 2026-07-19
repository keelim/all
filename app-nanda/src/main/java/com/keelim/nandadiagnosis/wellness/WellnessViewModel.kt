package com.keelim.nandadiagnosis.wellness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.WellnessRepository
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessGoal
import com.keelim.nandadiagnosis.wellness.domain.MeasurementState
import com.keelim.nandadiagnosis.wellness.domain.RoutineKind
import com.keelim.nandadiagnosis.wellness.domain.WellnessRules
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class WellnessViewModel @Inject constructor(
    private val repository: WellnessRepository,
) : ViewModel() {
    private val validationErrors = MutableStateFlow<List<String>>(emptyList())

    val uiState: StateFlow<WellnessUiState> =
        combine(repository.data, validationErrors) { data, errors ->
            WellnessUiState(
                measurements = data.measurements,
                routines = data.routines,
                completions = data.completions,
                goal = data.goal,
                validationErrors = errors,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = WellnessUiState(),
        )

    init {
        viewModelScope.launch { repository.initializeDefaultRoutines(LocalDate.now().toString()) }
    }

    fun saveMeasurement(
        length: String,
        circumference: String,
        state: MeasurementState,
        date: LocalDate = LocalDate.now(),
    ): Boolean {
        val lengthCm = WellnessRules.parseLengthCm(length)
        val circumferenceCm = WellnessRules.parseCircumferenceCm(circumference)
        if (lengthCm == null || circumferenceCm == null) {
            validationErrors.value = listOf("measurement")
            return false
        }

        validationErrors.value = emptyList()
        viewModelScope.launch {
            repository.upsertMeasurement(
                Measurement(
                    localDate = date.toString(),
                    lengthCm = lengthCm,
                    circumferenceCm = circumferenceCm,
                    state = state.name,
                ),
            )
        }
        return true
    }

    fun addRoutine(
        name: String,
        kind: RoutineKind,
        date: LocalDate,
    ): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            validationErrors.value = listOf("routineName")
            return false
        }

        validationErrors.value = emptyList()
        viewModelScope.launch {
            repository.insertRoutine(
                Routine(
                    name = trimmedName,
                    kind = kind.name,
                    createdLocalDate = date.toString(),
                ),
            )
        }
        return true
    }

    fun setGoal(
        metric: GoalMetric,
        target: String,
    ): Boolean {
        val targetCm =
            when (metric) {
                GoalMetric.LENGTH -> WellnessRules.parseLengthCm(target)
                GoalMetric.CIRCUMFERENCE -> WellnessRules.parseCircumferenceCm(target)
            }
        val current = uiState.value.measurements.maxByOrNull { it.localDate } ?: run {
            validationErrors.value = listOf("goalMeasurement")
            return false
        }
        if (targetCm == null) {
            validationErrors.value = listOf("goal")
            return false
        }
        validationErrors.value = emptyList()
        val baseline = if (metric == GoalMetric.LENGTH) current.lengthCm else current.circumferenceCm
        viewModelScope.launch {
            repository.upsertGoal(
                WellnessGoal(metric = metric.name, targetCm = targetCm, baselineCm = baseline),
            )
        }
        return true
    }

    fun clearGoal() {
        viewModelScope.launch { repository.deleteGoal() }
    }

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch { repository.deleteRoutine(routine) }
    }

    fun setRoutineCompletion(
        routine: Routine,
        date: LocalDate,
        checked: Boolean,
        duration: Int?,
    ) {
        val kind = RoutineKind.valueOf(routine.kind)
        val durationMinutes = duration.takeUnless { kind == RoutineKind.SUPPLEMENT }

        validationErrors.value = emptyList()
        viewModelScope.launch {
            val completion =
                RoutineCompletion(
                    routineId = routine.id,
                    localDate = date.toString(),
                    durationMinutes = durationMinutes,
                )
            if (checked) {
                if (!WellnessRules.isValidDuration(kind, durationMinutes)) {
                    validationErrors.value = listOf("duration")
                    return@launch
                }
                repository.upsertRoutineCompletion(completion)
            } else {
                repository.deleteRoutineCompletion(completion)
            }
        }
    }
}

data class WellnessUiState(
    val measurements: List<Measurement> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val goal: WellnessGoal? = null,
    val validationErrors: List<String> = emptyList(),
)

enum class GoalMetric { LENGTH, CIRCUMFERENCE }
