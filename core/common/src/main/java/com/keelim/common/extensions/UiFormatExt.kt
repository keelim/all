package com.keelim.common.extensions

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Number.toUiNumber(locale: Locale = Locale.getDefault()): String {
    return NumberFormat.getNumberInstance(locale).format(this)
}

fun Number.toUiCurrency(
    locale: Locale = Locale.getDefault(),
    currency: Currency? = null,
): String {
    val formatter = NumberFormat.getCurrencyInstance(locale)
    currency?.let { formatter.currency = it }
    return formatter.format(this)
}

fun Int.toUiTwoDigits(): String {
    return toString().padStart(2, '0')
}

fun Int.toUiAlignedTwoDigits(isLeadingZeroNeeded: Boolean): String {
    return toString().padStart(2, if (isLeadingZeroNeeded) '0' else ' ')
}

fun formatUiDate(year: Int, month: Int, day: Int): String {
    return "$year.${month.toUiTwoDigits()}.${day.toUiTwoDigits()}"
}

fun formatUiTime(hour: Int, minute: Int): String {
    return "${hour.toUiTwoDigits()}:${minute.toUiTwoDigits()}"
}

fun formatUiDateTime(
    year: Int,
    month: Int,
    day: Int,
    hour: Int,
    minute: Int,
): String {
    return "${formatUiDate(year, month, day)} ${formatUiTime(hour, minute)}"
}
