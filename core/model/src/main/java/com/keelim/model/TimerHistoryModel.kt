package com.keelim.model

data class TimerHistoryModel(
    val uid: Int,
    val date: String,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val description: String,
    val isCompleted: Boolean,
) {
    val formattedTime: String
        get() = buildString {
            if (hours > 0) append("${hours}h ")
            if (minutes > 0) append("${minutes}m ")
            if (seconds > 0) append("${seconds}s")
        }.trim().ifEmpty { "0s" }
}
