package com.keelim.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class KuiElevationScale(
    val none: Dp = 0.dp,
    val card: Dp = 1.dp,
    val soft: Dp = 3.dp,
    val panel: Dp = 6.dp,
)

val KuiElevation = KuiElevationScale()

val LocalKuiElevation = staticCompositionLocalOf { KuiElevation }
