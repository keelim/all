package com.keelim.comssa.ui.screen.main.ecocal

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.ComssaRoute

fun NavController.navigateEcocal(navOptions: NavOptions? = null) {
    this.navigate(ComssaRoute.Ecocal, navOptions)
}

fun NavGraphBuilder.ecocalScreen() {
    composable<ComssaRoute.Ecocal> {
        EcocalRoute()
    }
}
