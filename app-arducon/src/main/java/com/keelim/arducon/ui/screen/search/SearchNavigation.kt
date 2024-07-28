package com.keelim.arducon.ui.screen.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.ArduconRoute

fun NavController.navigateSearch(navOptions: NavOptions? = null) {
    this.navigate(ArduconRoute.Search, navOptions)
}

fun NavGraphBuilder.searchScreen(
) {
    composable<ArduconRoute.Search> {
        SearchRoute()
    }
}
