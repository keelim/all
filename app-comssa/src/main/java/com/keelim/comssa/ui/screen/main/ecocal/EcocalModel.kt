package com.keelim.comssa.ui.screen.main.ecocal

import com.keelim.model.EcoCalEntry
import com.keelim.model.day
import com.keelim.model.month

data class EcoCalModel(
    val country: String = "",
    val date: String = "",
    val priority: EcocalPriority = EcocalPriority.NONE,
    val time: String = "",
    val title: String = "",
) {
    val isToday: Boolean = (month in date && day in date)
}

fun EcoCalEntry.toModel() = EcoCalModel(
    country = country,
    date = date,
    priority = EcocalPriority.find(priority),
    time = time,
    title = title,
)


enum class EcocalPriority {
    HIGH, MEDIUM, LOW, NONE;

    companion object {
        fun find(priority: String): EcocalPriority = when (priority) {
            "상" -> HIGH
            "중" -> MEDIUM
            "하" -> LOW
            else -> NONE
        }
    }
}
