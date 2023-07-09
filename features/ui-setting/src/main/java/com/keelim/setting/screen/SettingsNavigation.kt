package com.keelim.setting.screen

import SettingsRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateSettings(navOptions: NavOptions? = null) {
    this.navigate("settings", navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = "settings",
    ) {
        SettingsRoute(
            onNotificationsClick = onNotificationsClick,
            onOpenSourceClick = onOpenSourceClick,
        )
    }
    nestedGraphs()
}
