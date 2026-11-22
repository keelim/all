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
