package com.keelim.setting.screen.event

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument


fun NavController.navigateEvent(
    eventId: Int = 0,
    navOptions: NavOptions? = null,
) {
    this.navigate("event?eventId=$eventId", navOptions)
}

fun NavGraphBuilder.eventScreen() {
    composable(
        route = "event?eventId={eventId}",
        arguments = listOf(navArgument("eventId") { defaultValue = 0 }),
    ) {
        EventRoute()
    }
}
