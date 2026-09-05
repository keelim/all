package com.keelim.model.wellness

enum class RecoveryGoalType {
    MORNING_ENERGY,
    SLEEP_RHYTHM,
    EXERCISE_HABIT,
    CONFIDENCE_AND_SEXUAL_WELLNESS,
    ALCOHOL_MANAGEMENT,
    SMOKING_CESSATION,
    GENERAL_RECOVERY,
}

enum class DailyTimeBudget {
    FIVE_MINUTES,
    FIFTEEN_MINUTES,
    THIRTY_MINUTES_OR_MORE,
}

data class RecoveryGoal(
    val type: RecoveryGoalType,
    val dailyTimeBudget: DailyTimeBudget,
    val startedLocalDate: String,
    val updatedLocalDate: String,
)

data class Measurement(
    val localDate: String,
    val lengthCm: Double,
    val circumferenceCm: Double,
    val state: String,
)

data class CheckInRecord(
    val localDate: String,
    val sleep: Int? = null,
    val stress: Int? = null,
    val energy: Int? = null,
    val desire: Int? = null,
    val confidence: Int? = null,
    val morningCondition: String? = null,
    val drankAlcohol: Boolean? = null,
    val didCardio: Boolean? = null,
    val hasDiscomfort: Boolean? = null,
    val note: String = "",
)

data class Routine(
    val id: Long = 0,
    val name: String,
    val kind: String,
    val createdLocalDate: String,
)

data class RoutineCompletion(
    val routineId: Long,
    val localDate: String,
    val durationMinutes: Int? = null,
)

data class WellnessGoal(
    val metric: String,
    val targetCm: Double,
    val baselineCm: Double,
)

data class WellnessData(
    val measurements: List<Measurement> = emptyList(),
    val checkIns: List<CheckInRecord> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val goal: WellnessGoal? = null,
    val recoveryGoal: RecoveryGoal? = null,
)
