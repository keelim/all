package com.keelim.compose.component.appbar

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun TransparentSystemBar(
    onDisposeEffect: () -> Unit = {}
) {
    val systemUiController = rememberSystemUiController()
    val isDarkMode = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, isDarkMode) {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = isDarkMode
        )

        onDispose {
            onDisposeEffect()
        }
    }
}
