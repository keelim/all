package com.keelim.arducon.ui.screen.inappbrowser

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.keelim.core.navigation.ArduconRoute

fun NavController.navigateToInAppBrowser(url: String) {
    navigate(ArduconRoute.InAppBrowser(url))
}

fun NavGraphBuilder.inAppBrowserScreen(
    onBackClick: () -> Unit
) {
    composable<ArduconRoute.InAppBrowser> { backStackEntry ->
        val url = backStackEntry.toRoute<ArduconRoute.InAppBrowser>().url
        InAppBrowserRoute(
            url = url,
            onBackClick = onBackClick
        )
    }
}
