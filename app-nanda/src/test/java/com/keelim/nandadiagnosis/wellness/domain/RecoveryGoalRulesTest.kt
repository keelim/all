package com.keelim.nandadiagnosis.wellness.domain

import com.keelim.model.wellness.DailyTimeBudget
import com.keelim.model.wellness.RecoveryGoalType
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class RecoveryGoalRulesTest : FunSpec({
    test("morning energy recommendations follow each time budget") {
        RecoveryGoalRules.recommendations(
            RecoveryGoalType.MORNING_ENERGY,
            DailyTimeBudget.FIVE_MINUTES,
        ).shouldContainExactly(
            RecoveryActionTemplate.MORNING_SUNLIGHT_5,
            RecoveryActionTemplate.AFTER_LUNCH_WALK_5,
            RecoveryActionTemplate.RECORD_WAKE_TIME,
        )

        RecoveryGoalRules.recommendations(
            RecoveryGoalType.MORNING_ENERGY,
            DailyTimeBudget.FIFTEEN_MINUTES,
        ).shouldContainExactly(
            RecoveryActionTemplate.AFTER_MEAL_WALK_10,
            RecoveryActionTemplate.CONSISTENT_WAKE_WINDOW,
            RecoveryActionTemplate.FULL_BODY_EXERCISE_10,
        )

        RecoveryGoalRules.recommendations(
            RecoveryGoalType.MORNING_ENERGY,
            DailyTimeBudget.THIRTY_MINUTES_OR_MORE,
        ).shouldContainExactly(
            RecoveryActionTemplate.BRISK_WALK_20,
            RecoveryActionTemplate.FULL_BODY_STRENGTH_20,
            RecoveryActionTemplate.EARLY_BEDTIME_PREP_30,
        )
    }

    test("every goal and budget returns one to three distinct actions") {
        RecoveryGoalType.entries.forEach { goalType ->
            DailyTimeBudget.entries.forEach { timeBudget ->
                val recommendations = RecoveryGoalRules.recommendations(goalType, timeBudget)

                (recommendations.size in 1..3) shouldBe true
                recommendations.distinct() shouldBe recommendations
            }
        }
    }

    test("non-morning goal catalogs expose every specified action across budgets") {
        val expectedCatalogs = mapOf(
            RecoveryGoalType.SLEEP_RHYTHM to setOf(
                RecoveryActionTemplate.CONSISTENT_WAKE_TIME,
                RecoveryActionTemplate.EARLY_BEDTIME_PREP_30,
                RecoveryActionTemplate.AVOID_LATE_CAFFEINE,
                RecoveryActionTemplate.SCREEN_FREE_20,
            ),
            RecoveryGoalType.EXERCISE_HABIT to setOf(
                RecoveryActionTemplate.WALK_5,
                RecoveryActionTemplate.BRISK_WALK_20,
                RecoveryActionTemplate.FULL_BODY_STRENGTH_10,
                RecoveryActionTemplate.SQUAT_PUSHUP_ONE_SET,
            ),
            RecoveryGoalType.CONFIDENCE_AND_SEXUAL_WELLNESS to setOf(
                RecoveryActionTemplate.BRISK_WALK_20,
                RecoveryActionTemplate.FULL_BODY_STRENGTH_15,
                RecoveryActionTemplate.SECURE_SLEEP_TIME,
                RecoveryActionTemplate.SLOW_BREATHING_5,
            ),
            RecoveryGoalType.ALCOHOL_MANAGEMENT to setOf(
                RecoveryActionTemplate.ALCOHOL_FREE_TODAY,
                RecoveryActionTemplate.NON_ALCOHOLIC_DRINK,
                RecoveryActionTemplate.EVENING_WALK_10,
                RecoveryActionTemplate.RECORD_ALCOHOL_USE,
            ),
            RecoveryGoalType.SMOKING_CESSATION to setOf(
                RecoveryActionTemplate.DELAY_FIRST_SMOKING_30,
                RecoveryActionTemplate.CRAVING_WALK_5,
                RecoveryActionTemplate.RECORD_SMOKING_USE,
                RecoveryActionTemplate.CHECK_SMOKING_SUPPORT,
            ),
            RecoveryGoalType.GENERAL_RECOVERY to setOf(
                RecoveryActionTemplate.BRISK_WALK_10,
                RecoveryActionTemplate.RECORD_WAKE_TIME,
                RecoveryActionTemplate.FULL_BODY_EXERCISE_10,
                RecoveryActionTemplate.SLOW_BREATHING_5,
            ),
        )

        expectedCatalogs.forEach { (goalType, expected) ->
            DailyTimeBudget.entries
                .flatMap { RecoveryGoalRules.recommendations(goalType, it) }
                .toSet() shouldBe expected
        }
    }

    test("weekly activity counts completions and unique local dates from Monday through today") {
        val summary = RecoveryGoalRules.weeklyActivitySummary(
            completionLocalDates = listOf(
                "2026-08-23",
                "2026-08-24",
                "2026-08-25",
                "2026-08-25",
                "2026-08-27",
                "not-a-date",
            ),
            today = LocalDate.of(2026, 8, 26),
        )

        summary shouldBe WeeklyActivitySummary(completionCount = 3, activeDays = 2)
    }
})
