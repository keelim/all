package com.keelim.setting.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateNotification(navOptions: NavOptions? = null) {
    this.navigate("notification", navOptions)
}

fun NavGraphBuilder.notificationScreen() {
    composable(
        route = "notification",
    ) {
        NotificationRoute()
    }
}
