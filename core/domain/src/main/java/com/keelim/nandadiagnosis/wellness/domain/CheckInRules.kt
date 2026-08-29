package com.keelim.nandadiagnosis.wellness.domain

import java.time.LocalDate

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

enum class MorningCondition {
    YES,
    NO,
    NOT_CHECKED,
}

enum class CheckInError {
    CONDITION_OUT_OF_RANGE,
}

object CheckInRules {
    fun validate(checkIn: DailyCheckIn): Set<CheckInError> =
        if (
            listOf(
                checkIn.sleep,
                checkIn.stress,
                checkIn.energy,
                checkIn.desire,
                checkIn.confidence,
            ).all { it in 1..5 }
        ) {
            emptySet()
        } else {
            setOf(CheckInError.CONDITION_OUT_OF_RANGE)
        }

    fun calculateStreak(
        localDates: Iterable<String>,
        today: LocalDate,
    ): Int {
        val dates =
            localDates
                .mapNotNull { runCatching { LocalDate.parse(it) }.getOrNull() }
                .filterNot { it.isAfter(today) }
                .toSet()
        val start =
            when {
                today in dates -> today
                today.minusDays(1) in dates -> today.minusDays(1)
                else -> return 0
            }

        return generateSequence(start) { it.minusDays(1) }
            .takeWhile { it in dates }
            .count()
    }
}
