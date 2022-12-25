package com.keelim.compose.navigation

sealed class SettingRoute(val route: String) {
    object DeveloperOptions: SettingRoute("developer")
    object Theme: SettingRoute("theme")
    object Oss: SettingRoute("oss")
    object Lab: SettingRoute("lab")
}
