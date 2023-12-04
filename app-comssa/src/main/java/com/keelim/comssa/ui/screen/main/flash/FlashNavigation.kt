package com.keelim.comssa.ui.screen.main.flash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.comssa.ui.screen.main.calendar.CalendarRoute

const val FlashRoute = "flash"

fun NavController.navigateFlash(navOptions: NavOptions? = null) {
    this.navigate(FlashRoute, navOptions)
}

fun NavGraphBuilder.flashScreen() {
    composable(
        route = FlashRoute,
    ) {
        CalendarRoute()
    }
}
