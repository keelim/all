package com.keelim.nandadiagnosis.wellness.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

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
