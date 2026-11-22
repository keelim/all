package com.keelim.common.extensions

fun String.toFormattedMoneyOrEmpty(): String {
    if (isEmpty()) return ""
    val raw = replace(",", "")
    val number = raw.toLongOrNull() ?: return this
    return number.toUiNumber()
}

fun String.toMoneyOrZero(): Double {
    return replace(",", "").toDoubleOrNull() ?: 0.0
}
