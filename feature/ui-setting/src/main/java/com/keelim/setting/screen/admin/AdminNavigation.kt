package com.keelim.setting.screen.admin

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateAdmin(
    navOptions: NavOptions? = null,
) {
    this.navigate(FeatureRoute.Admin)
}

fun NavGraphBuilder.adminScreen(
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<FeatureRoute.Admin> {
        AdminRoute()
    }
    nestedGraphs()
}
