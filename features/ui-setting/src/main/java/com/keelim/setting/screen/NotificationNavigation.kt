package com.keelim.setting.screen

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateNotification(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Notification, navOptions)
}

fun NavGraphBuilder.notificationScreen() {
    composable<FeatureRoute.Notification> {
        NotificationRoute()
    }
}
