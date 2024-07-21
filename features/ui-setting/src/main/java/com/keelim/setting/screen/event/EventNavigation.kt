package com.keelim.setting.screen.event

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateEvent(
    eventId: Int = 0,
    navOptions: NavOptions? = null,
) {
    this.navigate(FeatureRoute.Event(eventId), navOptions)
}

fun NavGraphBuilder.eventScreen() {
    composable<FeatureRoute.Event> {
        EventRoute()
    }
}
