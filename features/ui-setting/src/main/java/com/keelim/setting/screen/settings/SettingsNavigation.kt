package com.keelim.setting.screen.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateSettings(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Settings, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onThemeChangeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
    onAppUpdateClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<FeatureRoute.Settings> {
        SettingsRoute(
            onThemeChangeClick = onThemeChangeClick,
            onNotificationsClick = onNotificationsClick,
            onOpenSourceClick = onOpenSourceClick,
            onLabClick = onLabClick,
            onAppUpdateClick = onAppUpdateClick,
        )
    }
    nestedGraphs()
}
