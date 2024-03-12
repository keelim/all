package com.keelim.model

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

val month = Clock.System.now().toLocalDateTime(TimeZone.UTC).month.value.toString()
val day = Clock.System.now().toLocalDateTime(TimeZone.UTC).dayOfMonth.toString()
data class EcoCalEntry(
    val country: String = "",
    val date: String = "",
    val priority: String = "",
    val time: String = "",
    val title: String = "",
) {
    val isToday: Boolean = (month in date && day in date)
}
