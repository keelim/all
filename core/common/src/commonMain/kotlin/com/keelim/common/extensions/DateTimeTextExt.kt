package com.keelim.common.extensions

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
