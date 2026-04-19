package com.keelim.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun KeelimDesignSystemTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) DarkColorScheme else LightColorScheme
    val kuiColors = if (isDarkTheme) adminBwKuiColors else financeKuiColors

    CompositionLocalProvider(
        LocalKuiColors provides kuiColors,
        LocalKuiTypography provides financeKuiTypography,
        LocalKuiSpacing provides KuiSpacing,
        LocalKuiElevation provides KuiElevation,
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = KeelimTypography,
            shapes = KuiShapes,
            content = content,
        )
    }
}

object KuiTheme {
    val colorScheme: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val colors: KuiColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKuiColors.current

    val type: KuiTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKuiTypography.current

    val spacing: KuiSpacingScale
        @Composable
        @ReadOnlyComposable
        get() = LocalKuiSpacing.current

    val elevation: KuiElevationScale
        @Composable
        @ReadOnlyComposable
        get() = LocalKuiElevation.current
}
