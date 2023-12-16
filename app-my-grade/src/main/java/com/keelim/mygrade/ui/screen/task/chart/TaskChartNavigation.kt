package com.keelim.mygrade.ui.screen.task.chart

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val taskChartRoute = "task/chart"
fun NavController.navigateTaskChart(
    navOptions: NavOptions? = null,
) {
    this.navigate(taskChartRoute, navOptions)
}

fun NavGraphBuilder.taskChartScreen() {
    composable(
        route = taskChartRoute,
    ) {
        TaskChartRoute()
    }
}
