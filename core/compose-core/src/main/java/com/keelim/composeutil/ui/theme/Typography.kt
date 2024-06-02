package com.keelim.composeutil.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.keelim.compose.core.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)

private fun KeelimFont(name: String = "Noto Sans KR", weight: FontWeight): Font {
    return Font(
        googleFont = GoogleFont(name),
        fontProvider = provider,
        weight = weight,
    )
}

private val typography = Typography()

private val fontFamily = FontFamily(
    KeelimFont(weight = FontWeight.W100),
    KeelimFont(weight = FontWeight.W200),
    KeelimFont(weight = FontWeight.W300),
    KeelimFont(weight = FontWeight.W400),
    KeelimFont(weight = FontWeight.W500),
    KeelimFont(weight = FontWeight.W600),
    KeelimFont(weight = FontWeight.W700),
    KeelimFont(weight = FontWeight.W800),
    KeelimFont(weight = FontWeight.W900),
)

internal val keelimTypography = Typography(
    displayLarge = typography.displayLarge.copy(
        fontFamily = fontFamily,
    ),
    displayMedium = typography.displayMedium.copy(
        fontFamily = fontFamily,
    ),
    displaySmall = typography.displaySmall.copy(
        fontFamily = fontFamily,
    ),

    headlineLarge = typography.headlineLarge.copy(
        fontFamily = fontFamily,
    ),
    headlineMedium = typography.headlineMedium.copy(
        fontFamily = fontFamily,
    ),
    headlineSmall = typography.headlineSmall.copy(
        fontFamily = fontFamily,
    ),

    titleLarge = typography.titleLarge.copy(
        fontFamily = fontFamily,
    ),
    titleMedium = typography.titleMedium.copy(
        fontFamily = fontFamily,
    ),
    titleSmall = typography.titleSmall.copy(
        fontFamily = fontFamily,
    ),

    bodyLarge = typography.bodyLarge.copy(
        fontFamily = fontFamily,
    ),
    bodyMedium = typography.bodyMedium.copy(
        fontFamily = fontFamily,
    ),
    bodySmall = typography.bodySmall.copy(
        fontFamily = fontFamily,
    ),

    labelLarge = typography.labelLarge.copy(
        fontFamily = fontFamily,
    ),
    labelMedium = typography.labelMedium.copy(
        fontFamily = fontFamily,
    ),
    labelSmall = typography.labelSmall.copy(
        fontFamily = fontFamily,
    ),
)
