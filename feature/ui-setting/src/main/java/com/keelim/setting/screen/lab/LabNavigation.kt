package com.keelim.setting.screen.lab

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateLab(
    navOptions: NavOptions? = null,
) {
    this.navigate(FeatureRoute.Lab, navOptions)
}

fun NavGraphBuilder.labScreen() {
    composable<FeatureRoute.Lab> {
        LabRoute()
    }
}
