package com.keelim.mygrade.ui.screen.task.chart

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateTaskChart(
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.TaskChart, navOptions)
}

fun NavGraphBuilder.taskChartScreen() {
    composable<MyGradeRoute.TaskChart> {
        TaskChartRoute()
    }
}
