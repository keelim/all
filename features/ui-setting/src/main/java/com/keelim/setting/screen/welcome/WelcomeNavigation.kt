package com.keelim.setting.screen.welcome

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateWelcome(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Welcome, navOptions)
}

fun NavGraphBuilder.welcomeScreen(
    onNavigateMain: () -> Unit,
) {
    composable<FeatureRoute.Welcome> {
        WelcomeRoute(
            onNavigateMain = onNavigateMain,
        )
    }
}
