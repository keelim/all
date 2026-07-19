package com.keelim.nandadiagnosis.wellness.domain

import java.time.LocalDate

enum class MeasurementState {
    RELAXED,
    STRETCHED,
    MAXIMUM,
}

enum class RoutineKind {
    SUPPLEMENT,
    RUNNING,
    EXERCISE,
}

data class SevenDaySummary(
    val completedDays: Int,
    val eligibleDays: Int,
)

object WellnessRules {
    private val measurementPattern = Regex("^\\d+(?:[.,]\\d)?$")

    fun parseLengthCm(input: String): Double? = parseMeasurement(input, 1.0..40.0)

    fun parseCircumferenceCm(input: String): Double? = parseMeasurement(input, 1.0..25.0)

    fun isValidDuration(
        kind: RoutineKind,
        durationMinutes: Int?,
    ): Boolean =
        when (kind) {
            RoutineKind.SUPPLEMENT -> durationMinutes == null
            RoutineKind.RUNNING,
            RoutineKind.EXERCISE,
            -> durationMinutes == null || durationMinutes in 1..1_440
        }

    fun sevenDaySummary(
        today: LocalDate,
        createdLocalDate: LocalDate,
        completedLocalDates: Set<LocalDate>,
    ): SevenDaySummary {
        val windowStart = today.minusDays(6)
        val firstEligibleDate = maxOf(windowStart, createdLocalDate)
        if (firstEligibleDate > today) return SevenDaySummary(completedDays = 0, eligibleDays = 0)

        val eligibleDates = generateSequence(firstEligibleDate) { date ->
            date.plusDays(1).takeIf { it <= today }
        }.toSet()

        return SevenDaySummary(
            completedDays = completedLocalDates.count(eligibleDates::contains),
            eligibleDays = eligibleDates.size,
        )
    }

    private fun parseMeasurement(
        input: String,
        range: ClosedFloatingPointRange<Double>,
    ): Double? {
        val normalized = input.trim()
        if (!measurementPattern.matches(normalized)) return null

        return normalized.replace(',', '.').toDoubleOrNull()?.takeIf(range::contains)
    }
}
