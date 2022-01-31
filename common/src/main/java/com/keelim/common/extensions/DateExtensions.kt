package com.keelim.common.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.formatTo(dateFormat: String, tz: TimeZone = TimeZone.getDefault()): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault()).apply {
        timeZone = tz
    }
    return formatter.format(this)
}