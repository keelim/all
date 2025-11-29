package com.keelim.common.extensions

import java.text.DecimalFormat

fun String.toFormattedMoneyOrEmpty(): String {
    if (isEmpty()) return ""
    val raw = replace(",", "")
    val number = raw.toLongOrNull() ?: return this
    return DecimalFormat("#,###").format(number)
}

fun String.toMoneyOrZero(): Double {
    return replace(",", "").toDoubleOrNull() ?: 0.0
}

