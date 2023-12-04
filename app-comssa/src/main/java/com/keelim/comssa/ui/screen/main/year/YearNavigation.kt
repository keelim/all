package com.keelim.comssa.ui.screen.main.year

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val yearRoute = "year"

fun NavController.navigateYear(navOptions: NavOptions? = null) {
    this.navigate(yearRoute, navOptions)
}

fun NavGraphBuilder.yearScreen() {
    composable(
        route = yearRoute,
    ) {
    }
}
