package com.keelim.mygrade.ui.screen.task.add

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val taskAddRoute = "task/add"
fun NavController.navigateTaskAdd(navOptions: NavOptions? = null) {
    this.navigate(taskAddRoute, navOptions)
}

fun NavGraphBuilder.taskAddScreen(
    onAddFinish: () -> Unit,
) {
    composable(
        route = taskAddRoute,
    ) {
        TaskAddRoute(
            onAddFinish = onAddFinish
        )
    }
}
