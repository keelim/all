package com.keelim.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary colors
private val Primary40 = Color(0xFF6750A4)
private val OnPrimary40 = Color(0xFFFFFFFF)
private val PrimaryContainer40 = Color(0xFFEADDFF)
private val OnPrimaryContainer40 = Color(0xFF21005D)

private val Primary80 = Color(0xFFD0BCFF)
private val OnPrimary80 = Color(0xFF381E72)
private val PrimaryContainer80 = Color(0xFF4F378B)
private val OnPrimaryContainer80 = Color(0xFFEADDFF)

// Secondary colors
private val Secondary40 = Color(0xFF625B71)
private val OnSecondary40 = Color(0xFFFFFFFF)
private val SecondaryContainer40 = Color(0xFFE8DEF8)
private val OnSecondaryContainer40 = Color(0xFF1D192B)

private val Secondary80 = Color(0xFFCCC2DC)
private val OnSecondary80 = Color(0xFF332D41)
private val SecondaryContainer80 = Color(0xFF4A4458)
private val OnSecondaryContainer80 = Color(0xFFE8DEF8)

// Tertiary colors
private val Tertiary40 = Color(0xFF7D5260)
private val OnTertiary40 = Color(0xFFFFFFFF)
private val TertiaryContainer40 = Color(0xFFFFD8E4)
private val OnTertiaryContainer40 = Color(0xFF31111D)

private val Tertiary80 = Color(0xFFEFB8C8)
private val OnTertiary80 = Color(0xFF492532)
private val TertiaryContainer80 = Color(0xFF633B48)
private val OnTertiaryContainer80 = Color(0xFFFFD8E4)

// Error colors
private val Error40 = Color(0xFFB3261E)
private val OnError40 = Color(0xFFFFFFFF)
private val ErrorContainer40 = Color(0xFFF9DEDC)
private val OnErrorContainer40 = Color(0xFF410E0B)

private val Error80 = Color(0xFFF2B8B5)
private val OnError80 = Color(0xFF601410)
private val ErrorContainer80 = Color(0xFF8C1D18)
private val OnErrorContainer80 = Color(0xFFF9DEDC)

// Neutral colors
private val Background40 = Color(0xFFFFFBFE)
private val OnBackground40 = Color(0xFF1C1B1F)
private val Surface40 = Color(0xFFFFFBFE)
private val OnSurface40 = Color(0xFF1C1B1F)

private val Background80 = Color(0xFF1C1B1F)
private val OnBackground80 = Color(0xFFE6E1E5)
private val Surface80 = Color(0xFF1C1B1F)
private val OnSurface80 = Color(0xFFE6E1E5)

// Surface variants
private val SurfaceVariant40 = Color(0xFFE7E0EC)
private val OnSurfaceVariant40 = Color(0xFF49454F)
private val SurfaceVariant80 = Color(0xFF49454F)
private val OnSurfaceVariant80 = Color(0xFFCAC4D0)

// Outline
private val Outline40 = Color(0xFF79747E)
private val Outline80 = Color(0xFF938F99)

val LightColorScheme = lightColorScheme(
    primary = Primary40,
    onPrimary = OnPrimary40,
    primaryContainer = PrimaryContainer40,
    onPrimaryContainer = OnPrimaryContainer40,
    secondary = Secondary40,
    onSecondary = OnSecondary40,
    secondaryContainer = SecondaryContainer40,
    onSecondaryContainer = OnSecondaryContainer40,
    tertiary = Tertiary40,
    onTertiary = OnTertiary40,
    tertiaryContainer = TertiaryContainer40,
    onTertiaryContainer = OnTertiaryContainer40,
    error = Error40,
    onError = OnError40,
    errorContainer = ErrorContainer40,
    onErrorContainer = OnErrorContainer40,
    background = Background40,
    onBackground = OnBackground40,
    surface = Surface40,
    onSurface = OnSurface40,
    surfaceVariant = SurfaceVariant40,
    onSurfaceVariant = OnSurfaceVariant40,
    outline = Outline40,
)

val DarkColorScheme = darkColorScheme(
    primary = Primary80,
    onPrimary = OnPrimary80,
    primaryContainer = PrimaryContainer80,
    onPrimaryContainer = OnPrimaryContainer80,
    secondary = Secondary80,
    onSecondary = OnSecondary80,
    secondaryContainer = SecondaryContainer80,
    onSecondaryContainer = OnSecondaryContainer80,
    tertiary = Tertiary80,
    onTertiary = OnTertiary80,
    tertiaryContainer = TertiaryContainer80,
    onTertiaryContainer = OnTertiaryContainer80,
    error = Error80,
    onError = OnError80,
    errorContainer = ErrorContainer80,
    onErrorContainer = OnErrorContainer80,
    background = Background80,
    onBackground = OnBackground80,
    surface = Surface80,
    onSurface = OnSurface80,
    surfaceVariant = SurfaceVariant80,
    onSurfaceVariant = OnSurfaceVariant80,
    outline = Outline80,
)
