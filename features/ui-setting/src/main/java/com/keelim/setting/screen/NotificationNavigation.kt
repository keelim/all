package com.keelim.setting.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notificationRoute = "notification"
fun NavController.navigateNotification(navOptions: NavOptions? = null) {
    this.navigate(notificationRoute, navOptions)
}

fun NavGraphBuilder.notificationScreen() {
    composable(
        route = notificationRoute,
    ) {
        NotificationRoute()
    }
}
