package com.keelim.comssa.ui.screen.main.flash

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.comssa.ui.screen.main.calendar.CalendarRoute
import com.keelim.core.navigation.ComssaRoute

fun NavController.navigateFlash(navOptions: NavOptions? = null) {
    this.navigate(ComssaRoute.Flash, navOptions)
}

fun NavGraphBuilder.flashScreen() {
    composable<ComssaRoute.Flash> {
        CalendarRoute()
    }
}
