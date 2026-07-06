package com.keelim.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class KuiColors(
    // surface tokens not covered by M3 ColorScheme
    val surfaceSoft: Color,
    val surfaceStrong: Color,
    // status — success
    val success: Color,
    val successContainer: Color,
    val onSuccess: Color,
    val onSuccessContainer: Color,
    // status — warning
    val warning: Color,
    val warningContainer: Color,
    val onWarning: Color,
    val onWarningContainer: Color,
    // status — info
    val info: Color,
    val infoContainer: Color,
    val onInfo: Color,
    val onInfoContainer: Color,
    // Korean market semantics: 상승 = red, 하락 = blue
    val up: Color,
    val upSoft: Color,
    val down: Color,
    val downSoft: Color,
    val flat: Color,
    val flatSoft: Color,
)

// ── finance (light) ──────────────────────────────────────────────────────────
// Mirrors --kui-* values from all-web-ui/src/styles/styles.css :root block
val financeKuiColors = KuiColors(
    surfaceSoft   = Color(0x0A0F172A),  // rgb(15 23 42 / 0.04)
    surfaceStrong = Color(0x140F172A),  // rgb(15 23 42 / 0.08)

    success          = Color(0xFF15803D),  // hsl(142 72% 29%)
    successContainer = Color(0xFFDCFCE7),  // hsl(138 76% 97%)
    onSuccess        = Color(0xFFFFFFFF),
    onSuccessContainer = Color(0xFF052E16),

    warning          = Color(0xFFB45309),  // hsl(38 92% 50%) darkened for contrast
    warningContainer = Color(0xFFFEF9C3),  // hsl(55 92% 95%)
    onWarning        = Color(0xFFFFFFFF),
    onWarningContainer = Color(0xFF431407),

    info          = Color(0xFF0369A1),  // hsl(201 96% 32%)
    infoContainer = Color(0xFFE0F2FE),  // hsl(204 94% 94%)
    onInfo        = Color(0xFFFFFFFF),
    onInfoContainer = Color(0xFF082F49),

    // 상승 = red, 하락 = blue (Korean finance convention)
    up       = Color(0xFFDC2626),  // hsl(2 78% 55%) → red
    upSoft   = Color(0x1ADC2626),  // 10% alpha
    down     = Color(0xFF1D4ED8),  // hsl(214 85% 45%) → blue
    downSoft = Color(0x1A1D4ED8),  // 10% alpha
    flat     = Color(0xFF64748B),  // hsl(215.4 16.3% 46.9%)
    flatSoft = Color(0x1A64748B),
)

// ── admin-bw (dark) ───────────────────────────────────────────────────────────
val financeDarkKuiColors = KuiColors(
    surfaceSoft = Color(0x8C1E293B),
    surfaceStrong = Color(0xC71E293B),
    success = Color(0xFF4ADE80),
    successContainer = Color(0xFF052E16),
    onSuccess = Color(0xFF052E16),
    onSuccessContainer = Color(0xFFBBF7D0),
    warning = Color(0xFFFDE68A),
    warningContainer = Color(0xFF431407),
    onWarning = Color(0xFF431407),
    onWarningContainer = Color(0xFFFEF3C7),
    info = Color(0xFF93C5FD),
    infoContainer = Color(0xFF1E3A8A),
    onInfo = Color(0xFF082F49),
    onInfoContainer = Color(0xFFBFDBFE),
    up = Color(0xFFF87171),
    upSoft = Color(0xFF4B1616),
    down = Color(0xFF60A5FA),
    downSoft = Color(0xFF14284B),
    flat = Color(0xFFA5B4C8),
    flatSoft = Color(0xFF1E293B),
)

val adminBwKuiColors = KuiColors(
    surfaceSoft   = Color(0x1FFFFFFF),  // rgb(255 255 255 / 0.08)
    surfaceStrong = Color(0x33FFFFFF),  // rgb(255 255 255 / 0.12)

    success          = Color(0xFF4ADE80),
    successContainer = Color(0xFF052E16),
    onSuccess        = Color(0xFF052E16),
    onSuccessContainer = Color(0xFF4ADE80),

    warning          = Color(0xFFFBBF24),
    warningContainer = Color(0xFF431407),
    onWarning        = Color(0xFF431407),
    onWarningContainer = Color(0xFFFBBF24),

    info          = Color(0xFF38BDF8),
    infoContainer = Color(0xFF082F49),
    onInfo        = Color(0xFF082F49),
    onInfoContainer = Color(0xFF38BDF8),

    up       = Color(0xFFFF6B6B),  // dark-tuned red
    upSoft   = Color(0x1AFF6B6B),
    down     = Color(0xFF6AA8FF),  // dark-tuned blue
    downSoft = Color(0x1A6AA8FF),
    flat     = Color(0xFF94A3B8),
    flatSoft = Color(0x1A94A3B8),
)

val LocalKuiColors = staticCompositionLocalOf { financeKuiColors }
