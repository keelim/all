package com.keelim.nandadiagnosis.wellness.domain

import com.keelim.model.wellness.DailyTimeBudget
import com.keelim.model.wellness.RecoveryGoalType
import java.time.LocalDate

enum class RecoveryActionTemplate(
    val kind: RoutineKind,
    val estimatedMinutes: Int,
) {
    MORNING_SUNLIGHT_5(RoutineKind.CUSTOM, 5),
    AFTER_LUNCH_WALK_5(RoutineKind.RUNNING, 5),
    RECORD_WAKE_TIME(RoutineKind.SLEEP, 0),
    AFTER_MEAL_WALK_10(RoutineKind.RUNNING, 10),
    CONSISTENT_WAKE_WINDOW(RoutineKind.SLEEP, 0),
    FULL_BODY_EXERCISE_10(RoutineKind.EXERCISE, 10),
    BRISK_WALK_20(RoutineKind.RUNNING, 20),
    FULL_BODY_STRENGTH_20(RoutineKind.EXERCISE, 20),
    EARLY_BEDTIME_PREP_30(RoutineKind.SLEEP, 0),
    CONSISTENT_WAKE_TIME(RoutineKind.SLEEP, 0),
    AVOID_LATE_CAFFEINE(RoutineKind.SLEEP, 0),
    SCREEN_FREE_20(RoutineKind.SLEEP, 20),
    WALK_5(RoutineKind.RUNNING, 5),
    FULL_BODY_STRENGTH_10(RoutineKind.EXERCISE, 10),
    SQUAT_PUSHUP_ONE_SET(RoutineKind.EXERCISE, 5),
    FULL_BODY_STRENGTH_15(RoutineKind.EXERCISE, 15),
    SECURE_SLEEP_TIME(RoutineKind.SLEEP, 0),
    SLOW_BREATHING_5(RoutineKind.STRESS, 5),
    ALCOHOL_FREE_TODAY(RoutineKind.ALCOHOL, 0),
    NON_ALCOHOLIC_DRINK(RoutineKind.ALCOHOL, 0),
    EVENING_WALK_10(RoutineKind.RUNNING, 10),
    RECORD_ALCOHOL_USE(RoutineKind.ALCOHOL, 0),
    DELAY_FIRST_SMOKING_30(RoutineKind.SMOKING, 0),
    CRAVING_WALK_5(RoutineKind.RUNNING, 5),
    RECORD_SMOKING_USE(RoutineKind.SMOKING, 0),
    CHECK_SMOKING_SUPPORT(RoutineKind.SMOKING, 15),
    BRISK_WALK_10(RoutineKind.RUNNING, 10),
}

data class WeeklyActivitySummary(
    val completionCount: Int,
    val activeDays: Int,
)

object RecoveryGoalRules {
    private val catalogs = mapOf(
        RecoveryGoalType.SLEEP_RHYTHM to listOf(
            RecoveryActionTemplate.CONSISTENT_WAKE_TIME,
            RecoveryActionTemplate.EARLY_BEDTIME_PREP_30,
            RecoveryActionTemplate.AVOID_LATE_CAFFEINE,
            RecoveryActionTemplate.SCREEN_FREE_20,
        ),
        RecoveryGoalType.EXERCISE_HABIT to listOf(
            RecoveryActionTemplate.WALK_5,
            RecoveryActionTemplate.BRISK_WALK_20,
            RecoveryActionTemplate.FULL_BODY_STRENGTH_10,
            RecoveryActionTemplate.SQUAT_PUSHUP_ONE_SET,
        ),
        RecoveryGoalType.CONFIDENCE_AND_SEXUAL_WELLNESS to listOf(
            RecoveryActionTemplate.BRISK_WALK_20,
            RecoveryActionTemplate.FULL_BODY_STRENGTH_15,
            RecoveryActionTemplate.SECURE_SLEEP_TIME,
            RecoveryActionTemplate.SLOW_BREATHING_5,
        ),
        RecoveryGoalType.ALCOHOL_MANAGEMENT to listOf(
            RecoveryActionTemplate.ALCOHOL_FREE_TODAY,
            RecoveryActionTemplate.NON_ALCOHOLIC_DRINK,
            RecoveryActionTemplate.EVENING_WALK_10,
            RecoveryActionTemplate.RECORD_ALCOHOL_USE,
        ),
        RecoveryGoalType.SMOKING_CESSATION to listOf(
            RecoveryActionTemplate.DELAY_FIRST_SMOKING_30,
            RecoveryActionTemplate.CRAVING_WALK_5,
            RecoveryActionTemplate.RECORD_SMOKING_USE,
            RecoveryActionTemplate.CHECK_SMOKING_SUPPORT,
        ),
        RecoveryGoalType.GENERAL_RECOVERY to listOf(
            RecoveryActionTemplate.BRISK_WALK_10,
            RecoveryActionTemplate.RECORD_WAKE_TIME,
            RecoveryActionTemplate.FULL_BODY_EXERCISE_10,
            RecoveryActionTemplate.SLOW_BREATHING_5,
        ),
    )

    fun recommendations(
        type: RecoveryGoalType,
        timeBudget: DailyTimeBudget,
    ): List<RecoveryActionTemplate> =
        if (type == RecoveryGoalType.MORNING_ENERGY) {
            morningEnergyRecommendations(timeBudget)
        } else {
            catalogs.getValue(type)
                .filter { it.estimatedMinutes <= timeBudget.maxActionMinutes }
                .sortedByDescending(RecoveryActionTemplate::estimatedMinutes)
                .take(3)
        }

    fun weeklyActivitySummary(
        completionLocalDates: Iterable<String>,
        today: LocalDate,
    ): WeeklyActivitySummary {
        val weekStart = today.minusDays(today.dayOfWeek.value.toLong() - 1)
        val dates = completionLocalDates.mapNotNull { value ->
            runCatching { LocalDate.parse(value) }.getOrNull()
        }.filter { it in weekStart..today }

        return WeeklyActivitySummary(
            completionCount = dates.size,
            activeDays = dates.distinct().size,
        )
    }

    private fun morningEnergyRecommendations(timeBudget: DailyTimeBudget) =
        when (timeBudget) {
            DailyTimeBudget.FIVE_MINUTES -> listOf(
                RecoveryActionTemplate.MORNING_SUNLIGHT_5,
                RecoveryActionTemplate.AFTER_LUNCH_WALK_5,
                RecoveryActionTemplate.RECORD_WAKE_TIME,
            )
            DailyTimeBudget.FIFTEEN_MINUTES -> listOf(
                RecoveryActionTemplate.AFTER_MEAL_WALK_10,
                RecoveryActionTemplate.CONSISTENT_WAKE_WINDOW,
                RecoveryActionTemplate.FULL_BODY_EXERCISE_10,
            )
            DailyTimeBudget.THIRTY_MINUTES_OR_MORE -> listOf(
                RecoveryActionTemplate.BRISK_WALK_20,
                RecoveryActionTemplate.FULL_BODY_STRENGTH_20,
                RecoveryActionTemplate.EARLY_BEDTIME_PREP_30,
            )
        }

    private val DailyTimeBudget.maxActionMinutes: Int
        get() = when (this) {
            DailyTimeBudget.FIVE_MINUTES -> 5
            DailyTimeBudget.FIFTEEN_MINUTES -> 15
            DailyTimeBudget.THIRTY_MINUTES_OR_MORE -> 30
        }
}
