package com.keelim.mygrade.ui.screen.task.show

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val taskRoute = "task"

fun NavController.navigateTask(navOptions: NavOptions? = null) {
    this.navigate(taskRoute, navOptions)
}

fun NavController.navigateTaskPopUpTo() {
    this.navigate(taskRoute) {
        popUpTo(graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.taskScreen(
    onTaskClick: () -> Unit,
    onNavigateTaskClick: () -> Unit,
) {
    composable(
        route = taskRoute,
    ) {
        TaskRoute(
            onTaskClick = onTaskClick,
            onNavigateTaskClick = onNavigateTaskClick,
        )
    }
}
