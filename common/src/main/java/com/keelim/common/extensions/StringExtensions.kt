package com.keelim.common.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.toDate(dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss", tz: TimeZone = TimeZone.getDefault()): Date? {
    val parser = SimpleDateFormat(dateFormat, Locale.getDefault()).apply { timeZone = tz }
    return parser.parse(this)
}