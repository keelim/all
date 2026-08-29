package com.keelim.nandadiagnosis.wellness.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class CheckInRulesTest : FunSpec({
    test("condition values must stay on the five point scale") {
        CheckInRules.validate(checkIn()) shouldBe emptySet()
        CheckInRules.validate(checkIn(energy = 0)) shouldBe
            setOf(CheckInError.CONDITION_OUT_OF_RANGE)
    }

    test("insight needs five unique days and avoids causal language") {
        InsightCalculator.firstPattern(
            (1..4).map { day -> checkIn(localDate = "2026-07-0$day", sleep = 5, energy = 5) },
        ) shouldBe null

        InsightCalculator.firstPattern(
            (1..5).map { day -> checkIn(localDate = "2026-07-0$day", sleep = 5, energy = 4) },
        ) shouldBe PatternInsight(sampleDays = 5, kind = PatternKind.SLEEP_AND_ENERGY)
    }

    test("streak counts consecutive dates through today or yesterday") {
        CheckInRules.calculateStreak(
            localDates = listOf("2026-07-19", "2026-07-18", "2026-07-17", "2026-07-15"),
            today = LocalDate.of(2026, 7, 19),
        ) shouldBe 3

        CheckInRules.calculateStreak(
            localDates = listOf("2026-07-18", "2026-07-17"),
            today = LocalDate.of(2026, 7, 19),
        ) shouldBe 2
    }

    test("streak ignores duplicates future dates and invalid dates") {
        CheckInRules.calculateStreak(
            localDates =
                listOf(
                    "2026-07-19",
                    "2026-07-19",
                    "2026-07-18",
                    "2026-07-21",
                    "not-a-date",
                ),
            today = LocalDate.of(2026, 7, 19),
        ) shouldBe 2
    }

    test("streak is zero when today and yesterday are missing") {
        CheckInRules.calculateStreak(
            localDates = listOf("2026-07-17", "2026-07-16"),
            today = LocalDate.of(2026, 7, 19),
        ) shouldBe 0
    }
})

private fun checkIn(
    localDate: String = "2026-07-01",
    sleep: Int = 3,
    stress: Int = 3,
    energy: Int = 3,
) = DailyCheckIn(
    localDate = localDate,
    sleep = sleep,
    stress = stress,
    energy = energy,
    desire = 3,
    confidence = 3,
)
