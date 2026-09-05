package com.keelim.nandadiagnosis.wellness.domain

import java.time.LocalDate

data class DailyCheckIn(
    val localDate: String,
    val sleep: Int? = null,
    val stress: Int? = null,
    val energy: Int? = null,
    val desire: Int? = null,
    val confidence: Int? = null,
    val morningCondition: MorningCondition? = null,
    val drankAlcohol: Boolean? = null,
    val didCardio: Boolean? = null,
    val hasDiscomfort: Boolean? = null,
    val note: String = "",
)

enum class MorningCondition {
    YES,
    NO,
    NOT_CHECKED,
}

enum class CheckInError {
    CONDITION_OUT_OF_RANGE,
    EMPTY,
}

object CheckInRules {
    fun validate(checkIn: DailyCheckIn): Set<CheckInError> {
        val scores = listOf(checkIn.sleep, checkIn.stress, checkIn.energy, checkIn.desire, checkIn.confidence)
        if (scores.filterNotNull().any { it !in 1..5 }) return setOf(CheckInError.CONDITION_OUT_OF_RANGE)
        return if (scores.all { it == null } && checkIn.morningCondition == null &&
            checkIn.drankAlcohol == null && checkIn.didCardio == null &&
            checkIn.hasDiscomfort == null && checkIn.note.isBlank()
        ) setOf(CheckInError.EMPTY) else emptySet()
    }

    fun recentDates(today: LocalDate): List<LocalDate> =
        (6L downTo 0L).map { today.minusDays(it) }

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
