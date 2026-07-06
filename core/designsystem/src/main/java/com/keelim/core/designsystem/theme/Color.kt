package com.keelim.core.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// ── Finance (light) palette ───────────────────────────────────────────────────
// Mirrors --kui-* tokens from all-web-ui/src/styles/styles.css :root
private val Finance_Bg              = Color(0xFFFFFFFF)  // --kui-color-bg
private val Finance_Surface         = Color(0xFFFFFFFF)  // --kui-color-surface
private val Finance_SurfaceVariant  = Color(0xFFF1F5F9)  // hsl(214.3 31.8% 91.4%) ≈ border token
private val Finance_Accent          = Color(0xFF0F172A)  // hsl(222.2 47.4% 11.2%) --kui-color-accent
private val Finance_AccentInk       = Color(0xFFF8FAFC)  // hsl(210 40% 98%) --kui-color-accent-ink
private val Finance_Text            = Color(0xFF0A1628)  // hsl(222.2 84% 4.9%) --kui-color-text
private val Finance_Muted           = Color(0xFF64748B)  // hsl(215.4 16.3% 46.9%) --kui-color-muted
private val Finance_Border          = Color(0xFFE2E8F0)  // hsl(214.3 31.8% 91.4%) --kui-color-border
private val Finance_Error           = Color(0xFFDC2626)  // hsl(0 84% 51%) --kui-color-danger
private val Finance_ErrorContainer  = Color(0xFFFEE2E2)  // --kui-color-danger-bg
private val Finance_OnError         = Color(0xFFFFFFFF)
private val Finance_OnErrorContainer = Color(0xFF7F1D1D)

// ── Admin-BW (dark) palette ───────────────────────────────────────────────────
// Mirrors .theme-admin-bw in all-web-ui/src/styles/themes/admin-bw.css
private val AdminBw_Bg              = Color(0xFF0A0A0A)  // near-black
private val AdminBw_Surface         = Color(0xFF141414)
private val AdminBw_SurfaceVariant  = Color(0xFF262626)
private val AdminBw_Accent          = Color(0xFFFFFFFF)  // white on near-black
private val AdminBw_AccentInk       = Color(0xFF0A0A0A)
private val AdminBw_Text            = Color(0xFFF8FAFC)
private val AdminBw_Muted           = Color(0xFF94A3B8)
private val AdminBw_Border          = Color(0xFF334155)
private val AdminBw_Error           = Color(0xFFFF6B6B)
private val AdminBw_ErrorContainer  = Color(0xFF450A0A)
private val AdminBw_OnError         = Color(0xFF0A0A0A)
private val AdminBw_OnErrorContainer = Color(0xFFFF6B6B)

// ── Color schemes ─────────────────────────────────────────────────────────────

val financeLightColorScheme = lightColorScheme(
    primary              = Finance_Accent,
    onPrimary            = Finance_AccentInk,
    primaryContainer     = Finance_SurfaceVariant,
    onPrimaryContainer   = Finance_Text,
    secondary            = Finance_Muted,
    onSecondary          = Finance_Bg,
    secondaryContainer   = Finance_SurfaceVariant,
    onSecondaryContainer = Finance_Text,
    tertiary             = Finance_Muted,
    onTertiary           = Finance_Bg,
    tertiaryContainer    = Finance_SurfaceVariant,
    onTertiaryContainer  = Finance_Text,
    error                = Finance_Error,
    onError              = Finance_OnError,
    errorContainer       = Finance_ErrorContainer,
    onErrorContainer     = Finance_OnErrorContainer,
    background           = Finance_Bg,
    onBackground         = Finance_Text,
    surface              = Finance_Surface,
    onSurface            = Finance_Text,
    surfaceVariant       = Finance_SurfaceVariant,
    onSurfaceVariant     = Finance_Muted,
    outline              = Finance_Border,
)

val adminBwDarkColorScheme = darkColorScheme(
    primary              = AdminBw_Accent,
    onPrimary            = AdminBw_AccentInk,
    primaryContainer     = AdminBw_SurfaceVariant,
    onPrimaryContainer   = AdminBw_Text,
    secondary            = AdminBw_Muted,
    onSecondary          = AdminBw_Bg,
    secondaryContainer   = AdminBw_SurfaceVariant,
    onSecondaryContainer = AdminBw_Text,
    tertiary             = AdminBw_Muted,
    onTertiary           = AdminBw_Bg,
    tertiaryContainer    = AdminBw_SurfaceVariant,
    onTertiaryContainer  = AdminBw_Text,
    error                = AdminBw_Error,
    onError              = AdminBw_OnError,
    errorContainer       = AdminBw_ErrorContainer,
    onErrorContainer     = AdminBw_OnErrorContainer,
    background           = AdminBw_Bg,
    onBackground         = AdminBw_Text,
    surface              = AdminBw_Surface,
    onSurface            = AdminBw_Text,
    surfaceVariant       = AdminBw_SurfaceVariant,
    onSurfaceVariant     = AdminBw_Muted,
    outline              = AdminBw_Border,
)

// Backward-compat aliases — core/component/.../Theme.kt imports these names
val financeDarkColorScheme = darkColorScheme(
    primary = Color(0xFFF8FAFC),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = Color(0xFFF8FAFC),
    secondary = Color(0xFFA5B4C8),
    onSecondary = Color(0xFF0F172A),
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = Color(0xFFF8FAFC),
    tertiary = Color(0xFFA5B4C8),
    onTertiary = Color(0xFF0F172A),
    tertiaryContainer = Color(0xFF1E293B),
    onTertiaryContainer = Color(0xFFF8FAFC),
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = Color(0xFFFECACA),
    background = Color(0xFF020817),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF030A19),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0x8C1E293B),
    onSurfaceVariant = Color(0xFFA5B4C8),
    outline = Color(0xFF1E293B),
)

val LightColorScheme = financeLightColorScheme
val DarkColorScheme  = adminBwDarkColorScheme
