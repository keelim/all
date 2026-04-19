package com.keelim.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class KuiSpacingScale(
    val none: Dp = 0.dp,
    val space1: Dp = 4.dp,
    val space2: Dp = 8.dp,
    val space3: Dp = 12.dp,
    val space4: Dp = 16.dp,
    val space6: Dp = 24.dp,
    val space8: Dp = 32.dp,
    val space12: Dp = 48.dp,
    val space16: Dp = 64.dp,
    val componentSm: Dp = 40.dp,
    val componentMd: Dp = 44.dp,
    val componentLg: Dp = 48.dp,
    val cardPadding: Dp = 24.dp,
    val sectionGap: Dp = 16.dp,
)

val KuiSpacing = KuiSpacingScale()

val LocalKuiSpacing = staticCompositionLocalOf { KuiSpacing }
