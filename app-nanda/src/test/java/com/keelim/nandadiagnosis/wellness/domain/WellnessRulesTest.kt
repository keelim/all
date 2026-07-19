package com.keelim.nandadiagnosis.wellness.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class WellnessRulesTest :
    FunSpec({
        test("measurement states preserve the approved progression") {
            MeasurementState.entries shouldBe
                listOf(MeasurementState.RELAXED, MeasurementState.STRETCHED, MeasurementState.MAXIMUM)
        }

        test("measurement inputs accept comma or dot and at most one decimal") {
            WellnessRules.parseLengthCm("12,3") shouldBe 12.3
            WellnessRules.parseCircumferenceCm("9.5") shouldBe 9.5
            WellnessRules.parseLengthCm("12.34") shouldBe null
            WellnessRules.parseCircumferenceCm("9,55") shouldBe null
        }

        test("measurement bounds are inclusive") {
            WellnessRules.parseLengthCm("1") shouldBe 1.0
            WellnessRules.parseLengthCm("40") shouldBe 40.0
            WellnessRules.parseLengthCm("40.1") shouldBe null
            WellnessRules.parseCircumferenceCm("1") shouldBe 1.0
            WellnessRules.parseCircumferenceCm("25") shouldBe 25.0
            WellnessRules.parseCircumferenceCm("0.9") shouldBe null
        }

        test("goal progress is based on the distance from its baseline") {
            WellnessRules.goalProgress(baselineCm = 10.0, currentCm = 12.5, targetCm = 15.0) shouldBe 0.5f
            WellnessRules.goalProgress(baselineCm = 10.0, currentCm = 16.0, targetCm = 15.0) shouldBe 1f
            WellnessRules.goalProgress(baselineCm = 10.0, currentCm = 8.0, targetCm = 15.0) shouldBe 0f
        }

        test("invalid measurement inputs are rejected") {
            listOf("", "   ", "not-a-number", "0", "-1", "1.11", "40.1").forEach { input ->
                WellnessRules.parseLengthCm(input) shouldBe null
            }
            listOf("", "   ", "not-a-number", "0", "-1", "1,11", "25.1").forEach { input ->
                WellnessRules.parseCircumferenceCm(input) shouldBe null
            }
        }

        test("duration rules match each routine kind") {
            WellnessRules.isValidDuration(RoutineKind.SUPPLEMENT, null) shouldBe true
            WellnessRules.isValidDuration(RoutineKind.SUPPLEMENT, 1) shouldBe false
            WellnessRules.isValidDuration(RoutineKind.RUNNING, null) shouldBe true
            WellnessRules.isValidDuration(RoutineKind.RUNNING, 1) shouldBe true
            WellnessRules.isValidDuration(RoutineKind.EXERCISE, 1_440) shouldBe true
            WellnessRules.isValidDuration(RoutineKind.EXERCISE, 0) shouldBe false
            WellnessRules.isValidDuration(RoutineKind.RUNNING, 1_441) shouldBe false
        }

        test("seven day summary excludes dates before routine creation") {
            val today = LocalDate.of(2026, 7, 19)
            val summary =
                WellnessRules.sevenDaySummary(
                    today = today,
                    createdLocalDate = today.minusDays(2),
                    completedLocalDates = setOf(today.minusDays(4), today.minusDays(2), today),
                )

            summary shouldBe SevenDaySummary(completedDays = 2, eligibleDays = 3)
        }

        test("future routines have no eligible days") {
            val today = LocalDate.of(2026, 7, 19)

            WellnessRules.sevenDaySummary(
                today = today,
                createdLocalDate = today.plusDays(1),
                completedLocalDates = setOf(today),
            ) shouldBe SevenDaySummary(completedDays = 0, eligibleDays = 0)
        }

        test("seven day summary counts only mixed completions inside the full date window") {
            val today = LocalDate.of(2026, 3, 2)

            WellnessRules.sevenDaySummary(
                today = today,
                createdLocalDate = LocalDate.of(2026, 2, 1),
                completedLocalDates =
                    setOf(
                        LocalDate.of(2026, 2, 23),
                        LocalDate.of(2026, 2, 24),
                        LocalDate.of(2026, 2, 28),
                        LocalDate.of(2026, 3, 2),
                        LocalDate.of(2026, 3, 3),
                    ),
            ) shouldBe SevenDaySummary(completedDays = 3, eligibleDays = 7)
        }
    })
