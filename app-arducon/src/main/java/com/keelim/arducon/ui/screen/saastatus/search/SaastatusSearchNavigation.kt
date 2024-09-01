package com.keelim.arducon.ui.screen.saastatus.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.SaastatusRoute

fun NavController.navigateSaastatusSearch(navOptions: NavOptions? = null) {
    this.navigate(SaastatusRoute.Search, navOptions)
}

fun NavGraphBuilder.saastatusSearchScreen(
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<SaastatusRoute.Search> {
        SaastatusSearchRoute()
    }
    nestedGraphs()
}
