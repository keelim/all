package com.keelim.mygrade.ui.screen.task

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateTask(
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.Task, navOptions)
}

fun NavGraphBuilder.taskScreen(
    onNavigateChart: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<MyGradeRoute.Task> {
        TaskRoute(
            onNavigateChart = onNavigateChart,
        )
    }
    nestedGraphs()
}
