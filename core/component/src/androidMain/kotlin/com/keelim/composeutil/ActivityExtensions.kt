package com.keelim.composeutil

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.ui.theme.KeelimTheme
import com.keelim.shared.data.UserStateStore
import com.keelim.shared.data.model.ThemeType

/**
 * Sets the content of this activity with the KeelimTheme applied.
 * This extension provides a simplified way to set themed content without duplicating
 * theme handling logic across multiple activities.
 *
 * @param userStateStore Optional user state store for theme persistence. If null, light theme is used.
 * @param content The composable content to display, receives the WindowSizeClass.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun ComponentActivity.setThemeContent(
    userStateStore: UserStateStore? = null,
    content: @Composable (WindowSizeClass) -> Unit,
) {
    setContent {
        val isDarkTheme = if (userStateStore != null) {
            val themeType = userStateStore.themeTypeFlow
                .collectAsStateWithLifecycle(ThemeType.LIGHT)
                .value
            themeType.isDarkTheme()
        } else {
            false
        }

        KeelimTheme(
            isDarkTheme = isDarkTheme,
        ) {
            content(calculateWindowSizeClass(this@setThemeContent))
        }
    }
}
