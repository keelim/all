package com.keelim.compose.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink

@Composable
fun SchemeNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SchemeRoute.Share.route) {
        composable(
            route = SchemeRoute.Share.route,
            deepLinks = listOf(
                navDeepLink {
                    action = Intent.ACTION_SEND
                    mimeType = "text/*"
                },
                navDeepLink {
                    action = Intent.ACTION_SEND
                    mimeType = "image/*"
                }
            )
        ) {
        }
    }
}
