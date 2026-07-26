package com.keelim.nandadiagnosis.wellness

import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn

data class WellnessUiState(
    val measurements: List<Measurement> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val checkIns: List<DailyCheckIn> = emptyList(),
    val validationErrors: Set<WellnessValidationError> = emptySet(),
    val isLoading: Boolean = false,
)

enum class WellnessValidationError {
    CHECK_IN,
    MEASUREMENT,
    ROUTINE_NAME,
    DURATION,
}
