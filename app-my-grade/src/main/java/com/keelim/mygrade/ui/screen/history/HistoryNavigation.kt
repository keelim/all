package com.keelim.mygrade.ui.screen.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateHistory(navOptions: NavOptions? = null) {
    this.navigate(MyGradeRoute.History, navOptions)
}

fun NavGraphBuilder.historyScreen(
    onHistoryClick: (String, String, String) -> Unit,
) {
    composable<MyGradeRoute.History> {
        HistoryRoute(
            onHistoryClick = onHistoryClick,
        )
    }
}
