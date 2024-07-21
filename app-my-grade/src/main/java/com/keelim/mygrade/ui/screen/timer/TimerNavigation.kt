package com.keelim.mygrade.ui.screen.timer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateTimer(navOptions: NavOptions? = null) {
    this.navigate(MyGradeRoute.Timer, navOptions)
}

fun NavGraphBuilder.timerScreen() {
    composable<MyGradeRoute.Timer> {
    }
}
