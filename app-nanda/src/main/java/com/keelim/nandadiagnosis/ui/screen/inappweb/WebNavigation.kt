package com.keelim.nandadiagnosis.ui.screen.inappweb

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute
import com.keelim.setting.screen.webview.WebViewRoute

private const val defaultUri = "https://m.blog.naver.com/cjhdori"

fun NavController.navigateToWeb(uri: String, navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.Web, navOptions)
}

fun NavGraphBuilder.webScreen(
    onNavigateCategory: () -> Unit,
) {
    composable<NandaRoute.Web> {
        WebViewRoute(
            uri = defaultUri,
            onNavigateCategory = onNavigateCategory,
        )
    }
}
