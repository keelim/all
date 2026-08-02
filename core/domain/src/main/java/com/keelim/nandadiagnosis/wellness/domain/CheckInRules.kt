package com.keelim.nandadiagnosis.wellness.domain

import com.keelim.model.wellness.DailyCheckIn

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
