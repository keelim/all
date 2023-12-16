package com.keelim.ecocal

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ecocalRoute = "ecocal"
fun NavController.navigateEcocal(navOptions: NavOptions? = null) {
    this.navigate(ecocalRoute, navOptions)
}

fun NavGraphBuilder.ecocalScreen() {
    composable(
        route = ecocalRoute,
    ) {
        EcocalRoute()
    }
}
