package com.keelim.setting.screen.theme

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateTheme(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Theme, navOptions)
}

fun NavGraphBuilder.themeScreen() {
    composable<FeatureRoute.Theme> {
        ThemeRoute()
    }
}
