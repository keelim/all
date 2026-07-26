package com.keelim.nandadiagnosis.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NandaDarkColorScheme = darkColorScheme(
    primary = Color(0xFF42C4DD),
    onPrimary = Color(0xFF001F27),
    primaryContainer = Color(0xFF173D4A),
    onPrimaryContainer = Color(0xFFC5F3FC),
    secondary = Color(0xFFFFB000),
    onSecondary = Color(0xFF2B1700),
    background = Color(0xFF071625),
    onBackground = Color(0xFFF4F7FA),
    surface = Color(0xFF102132),
    onSurface = Color(0xFFF4F7FA),
    surfaceVariant = Color(0xFF172A3D),
    onSurfaceVariant = Color(0xFFAEBAC6),
    outline = Color(0xFF3A4E62),
    outlineVariant = Color(0xFF26394C),
    error = Color(0xFFFF8A80),
    onError = Color(0xFF3B0805),
)

@Composable
fun NandaTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = NandaDarkColorScheme, content = content)
}
