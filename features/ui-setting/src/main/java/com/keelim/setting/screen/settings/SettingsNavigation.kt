package com.keelim.setting.screen.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val settingsRoute = "settings"
fun NavController.navigateSettings(navOptions: NavOptions? = null) {
    this.navigate(settingsRoute, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onThemeChangeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
    onAppUpdateClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = settingsRoute,
    ) {
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
