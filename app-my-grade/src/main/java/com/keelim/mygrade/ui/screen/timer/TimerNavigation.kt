package com.keelim.mygrade.ui.screen.timer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val TimerRoute = "timer"
fun NavController.navigateTimer(navOptions: NavOptions? = null) {
    this.navigate(TimerRoute, navOptions)
}

fun NavGraphBuilder.timerScreen() {
    composable(
        route = TimerRoute,
    ) {
    }
}
