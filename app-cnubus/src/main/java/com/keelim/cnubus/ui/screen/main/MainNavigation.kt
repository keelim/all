package com.keelim.cnubus.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.CnuBusRoute

fun NavController.navigateMain(navOptions: NavOptions? = null) {
    this.navigate(CnuBusRoute.Main, navOptions)
}

fun NavGraphBuilder.mainScreen(
    onNavigateMap: () -> Unit,
    onNavigateAppSetting: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<CnuBusRoute.Main> {
        MainRoute(
            onNavigateMap = onNavigateMap,
            onNavigateAppSetting = onNavigateAppSetting,
        )
    }
    nestedGraphs()
}
