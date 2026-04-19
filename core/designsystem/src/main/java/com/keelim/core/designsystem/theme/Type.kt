package com.keelim.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val KeelimTypography = Typography(
    // display-xl / display-lg → financial hero numbers, splash headers
    displayLarge = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Bold,
        fontSize     = 72.sp,
        lineHeight   = 80.sp,
        letterSpacing = 0.sp,
    ),
    displayMedium = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Bold,
        fontSize     = 52.sp,
        lineHeight   = 60.sp,
        letterSpacing = 0.sp,
    ),
    // h1 — screen titles
    displaySmall = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Bold,
        fontSize     = 32.sp,
        lineHeight   = 40.sp,
        letterSpacing = 0.sp,
    ),
    // h2
    headlineLarge = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 24.sp,
        lineHeight   = 32.sp,
        letterSpacing = 0.sp,
    ),
    // h3
    headlineMedium = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 20.sp,
        lineHeight   = 28.sp,
        letterSpacing = 0.sp,
    ),
    // h4
    headlineSmall = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 16.sp,
        lineHeight   = 24.sp,
        letterSpacing = 0.sp,
    ),
    // section title — card headers
    titleLarge = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.SemiBold,
        fontSize     = 20.sp,
        lineHeight   = 28.sp,
        letterSpacing = 0.sp,
    ),
    // list item primary
    titleMedium = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Medium,
        fontSize     = 13.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Medium,
        fontSize     = 13.sp,
        lineHeight   = 18.sp,
        letterSpacing = 0.sp,
    ),
    // body — main reading text
    bodyLarge = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Normal,
        fontSize     = 17.sp,
        lineHeight   = 26.sp,
        letterSpacing = 0.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Normal,
        fontSize     = 15.sp,
        lineHeight   = 22.sp,
        letterSpacing = 0.sp,
    ),
    bodySmall = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Normal,
        fontSize     = 14.sp,
        lineHeight   = 20.sp,
        letterSpacing = 0.sp,
    ),
    // labels / captions / overlines
    labelLarge = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Medium,
        fontSize     = 13.sp,
        lineHeight   = 18.sp,
        letterSpacing = 0.sp,
    ),
    labelMedium = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Medium,
        fontSize     = 12.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.sp,
    ),
    // caption / overline — smallest readable text
    labelSmall = TextStyle(
        fontFamily   = KuiFontFamily.Sans,
        fontWeight   = FontWeight.Medium,
        fontSize     = 11.sp,
        lineHeight   = 16.sp,
        letterSpacing = 0.5.sp,
    ),
)
