package com.keelim.core.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val KuiShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),   // 0.5rem  — chips, tooltips
    small      = RoundedCornerShape(14.dp),  // 0.85rem — buttons, inputs
    medium     = RoundedCornerShape(18.dp),  // 1.15rem — input upper tier
    large      = RoundedCornerShape(24.dp),  // 1.5rem  — panels, cards
    extraLarge = RoundedCornerShape(28.dp),  // modal sheets
)
