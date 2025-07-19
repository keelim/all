@file:OptIn(ExperimentalTime::class)

package com.keelim.model

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

val month = Clock.System.now().toLocalDateTime(TimeZone.UTC).month.toString()
val day = Clock.System.now().toLocalDateTime(TimeZone.UTC).day.toString()

data class EcoCalEntry(
    val country: String = "",
    val date: String = "",
    val priority: String = "",
    val time: String = "",
    val title: String = "",
) {
    val isToday: Boolean = (month in date && day in date)
}
