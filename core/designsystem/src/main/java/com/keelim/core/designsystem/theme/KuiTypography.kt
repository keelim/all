package com.keelim.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

data class KuiTypography(
    // tabular numerics — financial figures, prices, counters
    val numeric: TextStyle,
    // large display ticker — stock price change row
    val ticker: TextStyle,
    // ALL CAPS overline label (section headers)
    val overline: TextStyle,
    // monospace code / key-value
    val code: TextStyle,
)

val financeKuiTypography = KuiTypography(
    numeric = TextStyle(
        fontFamily = KuiFontFamily.Numeric,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
    ),
    ticker = TextStyle(
        fontFamily = KuiFontFamily.Numeric,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.em,
    ),
    overline = TextStyle(
        fontFamily = KuiFontFamily.Sans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.12.em,
    ),
    code = TextStyle(
        fontFamily = KuiFontFamily.Mono,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.01.em,
    ),
)

val LocalKuiTypography = staticCompositionLocalOf { financeKuiTypography }
