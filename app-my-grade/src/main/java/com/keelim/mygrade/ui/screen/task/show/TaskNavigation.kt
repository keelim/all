package com.keelim.mygrade.ui.screen.task.show

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val taskRoute = "task"

fun NavController.navigateTask(navOptions: NavOptions) {
    this.navigate(taskRoute, navOptions)
}

fun NavController.navigateTask() {
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
        route = "Task",
    ) {
        TaskRoute(
            onTaskClick = onTaskClick,
            onNavigateTaskClick = onNavigateTaskClick,
        )
    }
}
