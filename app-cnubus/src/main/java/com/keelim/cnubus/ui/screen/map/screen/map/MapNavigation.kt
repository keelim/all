package com.keelim.cnubus.ui.screen.map.screen.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val mapRoute = "map"
fun NavController.navigateMap(navOptions: NavOptions? = null) {
    this.navigate(mapRoute, navOptions)
}

fun NavGraphBuilder.mapScreen() {
    composable(
        route = mapRoute,
    ) {
        MapRoute()
    }
}
