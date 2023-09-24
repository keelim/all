package com.keelim.mygrade.ui.screen.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateTask(navOptions: NavOptions? = null) {
    this.navigate("Task", navOptions)
}

fun NavGraphBuilder.taskScreen(
    onTaskClick: () -> Unit,
) {
    composable(
        route = "Task",
    ) {
        TaskRoute(
            onTaskClick = onTaskClick,
        )
    }
}
