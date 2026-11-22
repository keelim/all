package com.keelim.commonAndroid.extensions

import com.keelim.common.extensions.formatUiDate
import com.keelim.common.extensions.formatUiDateTime
import com.keelim.common.extensions.formatUiTime
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime

fun Instant.toUiDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return toLocalDateTime(timeZone).toUiDate()
}

fun Instant.toUiDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): String {
    return toLocalDateTime(timeZone).toUiDateTime()
}

fun LocalDateTime.toUiDateTime(): String {
    return formatUiDateTime(
        year = year,
        month = month.number,
        day = day,
        hour = hour,
        minute = minute,
    )
}

fun LocalDateTime.toUiDate(): String {
    return formatUiDate(
        year = year,
        month = month.number,
        day = day,
    )
}

fun LocalDateTime.toUiTime(): String {
    return formatUiTime(hour = hour, minute = minute)
}
