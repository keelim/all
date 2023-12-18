package com.keelim.cnubus.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val mainRoute = "main"
fun NavController.navigateMain(navOptions: NavOptions? = null) {
    this.navigate(mainRoute, navOptions)
}

fun NavGraphBuilder.mainScreen(
    onNavigateMap: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = mainRoute,
    ) {
        MainRoute(
            onNavigateMap = onNavigateMap,
        )
    }
    nestedGraphs()
}
