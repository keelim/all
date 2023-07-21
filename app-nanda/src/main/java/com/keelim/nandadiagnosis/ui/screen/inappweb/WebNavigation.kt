package com.keelim.nandadiagnosis.ui.screen.inappweb

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.keelim.setting.screen.webview.WebViewRoute

const val webRoute = "web"
private const val defaultUri = "https://m.blog.naver.com/cjhdori"
fun NavController.navigateToWeb(uri: String, navOptions: NavOptions? = null) {
    this.navigate("$webRoute/$uri", navOptions)
}

fun NavGraphBuilder.webScreen() {
    composable(
        route = "$webRoute/{uri}",
        arguments = listOf(
            navArgument("uri") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val uri = backStackEntry.arguments?.getString("uri", defaultUri) ?: defaultUri
        WebViewRoute(
            uri = uri
        )
    }
}
