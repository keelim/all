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
    onNotificationsClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = settingsRoute,
    ) {
        SettingsRoute(
            onNotificationsClick = onNotificationsClick,
            onOpenSourceClick = onOpenSourceClick,
        )
    }
    nestedGraphs()
}
