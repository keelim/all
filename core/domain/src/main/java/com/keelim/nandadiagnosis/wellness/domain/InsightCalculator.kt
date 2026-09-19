package com.keelim.nandadiagnosis.wellness.domain

data class PatternInsight(
    val sampleDays: Int,
    val kind: PatternKind,
)

enum class PatternKind {
    SLEEP_AND_ENERGY,
}

object InsightCalculator {
    private const val MINIMUM_DAYS = 5

    fun firstPattern(checkIns: List<DailyCheckIn>): PatternInsight? {
        val uniqueDays = checkIns.distinctBy(DailyCheckIn::localDate)
            .filter { it.sleep != null && it.energy != null }
        if (uniqueDays.size < MINIMUM_DAYS) return null

        val highSleepDays = uniqueDays.filter { it.sleep?.let { value -> value >= 4 } == true }
        if (highSleepDays.size < 3) return null
        val highEnergyCount = highSleepDays.count { it.energy?.let { value -> value >= 4 } == true }
        return if (highEnergyCount * 2 >= highSleepDays.size) {
            PatternInsight(uniqueDays.size, PatternKind.SLEEP_AND_ENERGY)
        } else {
            null
        }
    }
}
