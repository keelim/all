package com.keelim.cnubus.ui.screen.map.screen.map

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.CnuBusRoute

fun NavController.navigateMap(navOptions: NavOptions? = null) {
    this.navigate(CnuBusRoute.Map, navOptions)
}

fun NavGraphBuilder.mapScreen() {
    composable<CnuBusRoute.Map> {
        MapRoute()
    }
}
