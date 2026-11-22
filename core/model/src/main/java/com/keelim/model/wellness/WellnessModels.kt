package com.keelim.model.wellness

data class Measurement(
    val localDate: String,
    val lengthCm: Double,
    val circumferenceCm: Double,
    val state: String,
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

data class DailyCheckIn(
    val localDate: String,
    val sleep: Int,
    val stress: Int,
    val energy: Int,
    val desire: Int,
    val confidence: Int,
    val morningCondition: MorningCondition = MorningCondition.NOT_CHECKED,
    val drankAlcohol: Boolean = false,
    val didCardio: Boolean = false,
    val hasDiscomfort: Boolean = false,
    val note: String = "",
)

data class CareCheckIn(
    val localDate: String,
    val condition: CareCondition,
    val sweat: Boolean = false,
    val friction: Boolean = false,
    val dryness: Boolean = false,
)

enum class CareCondition {
    COMFORTABLE,
    AWARE,
    NEEDS_ATTENTION,
}

enum class MorningCondition {
    YES,
    NO,
    NOT_CHECKED,
}

data class WellnessGoal(
    val metric: String,
    val targetCm: Double,
    val baselineCm: Double,
)

data class WellnessData(
    val measurements: List<Measurement> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val checkIns: List<DailyCheckIn> = emptyList(),
    val careCheckIns: List<CareCheckIn> = emptyList(),
    val goal: WellnessGoal? = null,
)
