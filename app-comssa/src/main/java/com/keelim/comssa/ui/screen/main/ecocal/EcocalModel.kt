package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.vector.ImageVector
import com.keelim.composeutil.component.fab.FabButtonItem
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

data class High(
    override val imageVector: ImageVector = Icons.AutoMirrored.Filled.List,
    override val label: String = "상",
) : FabButtonItem

data class Medium(
    override val imageVector: ImageVector = Icons.AutoMirrored.Filled.List,
    override val label: String = "중",
) : FabButtonItem

data class Low(
    override val imageVector: ImageVector = Icons.AutoMirrored.Filled.List,
    override val label: String = "하",
) : FabButtonItem

data class Clear(
    override val imageVector: ImageVector = Icons.Filled.Close,
    override val label: String = "초기화",
) : FabButtonItem
