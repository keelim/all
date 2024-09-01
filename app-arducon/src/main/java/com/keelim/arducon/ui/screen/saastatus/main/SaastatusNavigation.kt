package com.keelim.arducon.ui.screen.saastatus.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.SaastatusRoute

fun NavController.navigateSaastatus(navOptions: NavOptions? = null) {
    this.navigate(SaastatusRoute.Main, navOptions)
}

fun NavGraphBuilder.saastatusScreen(
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<SaastatusRoute.Main> {

    }
    nestedGraphs()
}
