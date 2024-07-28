package com.keelim.arducon.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.ArduconRoute

fun NavController.navigateMain(navOptions: NavOptions? = null) {
    this.navigate(ArduconRoute.Main, navOptions)
}

fun NavGraphBuilder.mainScreen(
    onShowMessage: (String) -> Unit,
    onQrCodeClick: () -> Unit,
    onNavigateSearch: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<ArduconRoute.Main> {
        MainRoute(
            onShowMessage = onShowMessage,
            onQrCodeClick = onQrCodeClick,
            onNavigateSearch = onNavigateSearch,
        )
    }
    nestedGraphs()
}
