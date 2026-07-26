package com.keelim.nandadiagnosis.wellness.domain

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
}
