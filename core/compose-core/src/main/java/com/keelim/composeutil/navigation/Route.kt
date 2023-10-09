package com.keelim.composeutil.navigation

sealed class SettingRoute(val route: String) {
    object DeveloperOptions : SettingRoute("developer")
    object Theme : SettingRoute("theme")
    object Oss : SettingRoute("oss")
    object Lab : SettingRoute("lab")
}

sealed class SchemeRoute(val route: String) {
    object Share : SchemeRoute("share")
}