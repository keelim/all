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

data class WellnessGoal(
    val metric: String,
    val targetCm: Double,
    val baselineCm: Double,
)

data class WellnessData(
    val measurements: List<Measurement> = emptyList(),
    val routines: List<Routine> = emptyList(),
    val completions: List<RoutineCompletion> = emptyList(),
    val goal: WellnessGoal? = null,
)
