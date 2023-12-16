package com.keelim.mygrade.ui.screen.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val taskRoute = "task"
fun NavController.navigateTask(
    navOptions: NavOptions? = null,
) {
    this.navigate(taskRoute, navOptions)
}

fun NavGraphBuilder.taskScreen(
    onNavigateChart: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = taskRoute,
    ) {
        TaskRoute(
            onNavigateChart = onNavigateChart,
        )
    }
    nestedGraphs()
}
