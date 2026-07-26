package com.keelim.nandadiagnosis.wellness.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.keelim.nandadiagnosis.R

enum class WellnessDestination(
    @StringRes val labelRes: Int,
    val icon: ImageVector,
) {
    TODAY(R.string.wellness_tab_today, Icons.Filled.Home),
    PLAN(R.string.wellness_tab_plan, Icons.Filled.DateRange),
    INSIGHTS(R.string.wellness_tab_insights, Icons.Filled.Info),
    TOOLS(R.string.wellness_tab_tools, Icons.Filled.Settings),
}
