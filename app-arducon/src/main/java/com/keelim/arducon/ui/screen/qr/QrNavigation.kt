package com.keelim.arducon.ui.screen.qr

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.ArduconRoute

fun NavController.navigateQr(navOptions: NavOptions? = null) {
    this.navigate(ArduconRoute.Qr, navOptions)
}

fun NavGraphBuilder.qrScreen(
    onShowBarcode: (String) -> Unit,
) {
    composable<ArduconRoute.Qr> {
        QrRoute(
            onShowBarcode = onShowBarcode,
        )
    }
}
