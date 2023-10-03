package com.keelim.mygrade.ui.screen.timer.history

import TimerHistoryRoute
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val timerHistoryRoute = "timer/history"
fun NavController.navigateTimerHistory(navOptions: NavOptions? = null) {
    this.navigate(timerHistoryRoute, navOptions)
}

fun NavGraphBuilder.timerHistoryScreen() {
    composable(
        route = timerHistoryRoute,
    ) {
        TimerHistoryRoute()
    }
}
