package com.keelim.setting.screen.lab

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val labRoute = "lab"

fun NavController.navigateLab(
    navOptions: NavOptions? = null,
) {
    this.navigate(labRoute, navOptions)
}

fun NavGraphBuilder.labScreen() {
    composable(
        route = labRoute,
    ) {
        LabRoute()
    }
}
