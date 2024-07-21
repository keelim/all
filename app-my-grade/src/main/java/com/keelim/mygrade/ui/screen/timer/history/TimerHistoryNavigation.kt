package com.keelim.mygrade.ui.screen.timer.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateTimerHistory(navOptions: NavOptions? = null) {
    this.navigate(MyGradeRoute.TimerHistory, navOptions)
}

fun NavGraphBuilder.timerHistoryScreen() {
    composable<MyGradeRoute.TimerHistory> {
        TimerHistoryRoute()
    }
}
