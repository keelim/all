package com.keelim.arducon.ui.screen.qr

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val qrRoute = "qr"

fun NavController.navigateQr(navOptions: NavOptions? = null) {
    this.navigate(qrRoute, navOptions)
}

fun NavGraphBuilder.qrScreen(
    onShowBarcode: (String) -> Unit,
) {
    composable(
        route = qrRoute,
    ) {
        QrRoute(
            onShowBarcode = onShowBarcode
        )
    }
}
