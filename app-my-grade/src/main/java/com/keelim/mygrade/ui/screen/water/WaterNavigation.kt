package com.keelim.mygrade.ui.screen.water

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val waterRoute = "water"

fun NavController.navigateWater(navOptions: NavOptions? = null) {
    this.navigate(waterRoute, navOptions)
}

fun NavGraphBuilder.waterScreen(

) {
    composable(route = waterRoute) {
        WaterRoute()
    }
}
