package com.keelim.nandadiagnosis.wellness

import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.RecoveryGoal
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.nandadiagnosis.wellness.domain.DailyCheckIn

data class WellnessUiState(
    val measurements: List<Measurement> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val checkIns: List<DailyCheckIn> = emptyList(),
    val currentStreak: Int = 0,
    val recoveryGoal: RecoveryGoal? = null,
    val weeklyActionCompletions: Int = 0,
    val weeklyActiveDays: Int = 0,
    val validationErrors: Set<WellnessValidationError> = emptySet(),
    val today: java.time.LocalDate = java.time.LocalDate.now(),
    val isCheckInWriting: Boolean = false,
    val isLoading: Boolean = false,
)

enum class WellnessValidationError {
    CHECK_IN,
    CHECK_IN_STORAGE,
    MEASUREMENT,
    ROUTINE_NAME,
    DURATION,
}
