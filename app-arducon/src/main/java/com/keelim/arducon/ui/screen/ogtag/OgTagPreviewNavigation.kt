package com.keelim.arducon.ui.screen.ogtag

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.arducon.ui.screen.OgTagPreviewRoute
import com.keelim.core.navigation.ArduconRoute

fun NavController.navigateOgTagPreview(navOptions: NavOptions? = null) {
    this.navigate(ArduconRoute.OgTagPreview, navOptions)
}

fun NavGraphBuilder.ogTagPreviewScreen() {
    composable<ArduconRoute.OgTagPreview> {
        OgTagPreviewRoute()
    }
}
