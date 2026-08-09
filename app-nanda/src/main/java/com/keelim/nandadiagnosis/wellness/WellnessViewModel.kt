package com.keelim.nandadiagnosis.wellness

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.WellnessRepository
import com.keelim.common.platform.time.TimeProvider
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.wellness.domain.CheckInRules
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn
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
    private val timeProvider: TimeProvider,
) : ViewModel() {
    private val validationErrors = MutableStateFlow<Set<WellnessValidationError>>(emptySet())
    private val checkIns = MutableStateFlow<List<DailyCheckIn>>(emptyList())

    val uiState: StateFlow<WellnessUiState> =
        combine(repository.data, validationErrors, checkIns) { data, errors, dailyCheckIns ->
            WellnessUiState(
                measurements = data.measurements,
                routines = data.routines,
                completions = data.completions,
                checkIns = dailyCheckIns,
                validationErrors = errors,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = WellnessUiState(),
        )

    init {
        viewModelScope.launch { repository.initializeDefaultRoutines(timeProvider.today().toString()) }
    }

    fun saveCheckIn(checkIn: DailyCheckIn): Boolean {
        if (CheckInRules.validate(checkIn).isNotEmpty()) {
            validationErrors.value = setOf(WellnessValidationError.CHECK_IN)
            return false
        }
        validationErrors.value = emptySet()
        checkIns.value = checkIns.value.filterNot { it.localDate == checkIn.localDate } + checkIn
        return true
    }

    fun saveMeasurement(
        length: String,
        circumference: String,
        state: MeasurementState,
        date: LocalDate = timeProvider.today(),
    ): Boolean {
        val lengthCm = WellnessRules.parseLengthCm(length)
        val circumferenceCm = WellnessRules.parseCircumferenceCm(circumference)
        if (lengthCm == null || circumferenceCm == null) {
            validationErrors.value = setOf(WellnessValidationError.MEASUREMENT)
            return false
        }

        validationErrors.value = emptySet()
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
        date: LocalDate = timeProvider.today(),
    ): Boolean {
        val trimmedName = name.trim()
        if (trimmedName.isEmpty()) {
            validationErrors.value = setOf(WellnessValidationError.ROUTINE_NAME)
            return false
        }

        validationErrors.value = emptySet()
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

    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch { repository.deleteRoutine(routine) }
    }

    fun setRoutineCompletion(
        routine: Routine,
        date: LocalDate = timeProvider.today(),
        checked: Boolean,
        duration: Int?,
    ) {
        val kind = RoutineKind.valueOf(routine.kind)
        val durationMinutes = duration.takeUnless { kind == RoutineKind.SUPPLEMENT }

        validationErrors.value = emptySet()
        viewModelScope.launch {
            val completion =
                RoutineCompletion(
                    routineId = routine.id,
                    localDate = date.toString(),
                    durationMinutes = durationMinutes,
                )
            if (checked) {
                if (!WellnessRules.isValidDuration(kind, durationMinutes)) {
                    validationErrors.value = setOf(WellnessValidationError.DURATION)
                    return@launch
                }
                repository.upsertRoutineCompletion(completion)
            } else {
                repository.deleteRoutineCompletion(completion)
            }
        }
    }
}
