package com.keelim.setting.screen.theme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val themeRoute = "Theme/theme"
fun NavController.navigateTheme(navOptions: NavOptions? = null) {
    this.navigate(themeRoute, navOptions)
}

fun NavGraphBuilder.themeScreen() {
    composable(
        route = themeRoute,
    ) {
        ThemeRoute()
    }
}
