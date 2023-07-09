package com.keelim.mygrade.ui.screen.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.mygrade.ui.screen.main.NormalProbability

fun NavController.navigateHistory(navOptions: NavOptions? = null) {
    this.navigate("history", navOptions)
}

fun NavGraphBuilder.historyScreen(
    onHistoryClick: (String, String) -> Unit,
) {
    composable(
        route = "history",
    ) {
        HistoryRoute(
            onHistoryClick = onHistoryClick,
        )
    }
}
