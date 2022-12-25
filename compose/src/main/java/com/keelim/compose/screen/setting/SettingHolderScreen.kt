package com.keelim.compose.screen.setting

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.keelim.compose.navigation.SettingRoute

@Composable
fun SettingHolderScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = SettingRoute.DeveloperOptions.route
    ) {
        composable(route = SettingRoute.Lab.route) {
            LabScreen(navController = navController)
        }
        composable(route = SettingRoute.Theme.route) {
            ThemeScreen(navController = navController)
        }
        composable(route = SettingRoute.DeveloperOptions.route) {
            DeveloperScreen(navController = navController)
        }
        composable(route = SettingRoute.Oss.route) {
            OssScreen(navController = navController)
        }
    }
}
