package com.keelim.mygrade.ui.screen.history

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

fun NavController.navigateHistory(navOptions: NavOptions? = null) {
    this.navigate("history", navOptions)
}

fun NavGraphBuilder.historyScreen(
    onHistoryClick: (String, String, String) -> Unit,
) {
    composable(
        route = "history",
    ) {
        HistoryRoute(
            onHistoryClick = onHistoryClick,
        )
    }
}
