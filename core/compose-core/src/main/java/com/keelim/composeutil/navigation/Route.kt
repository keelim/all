package com.keelim.composeutil.navigation

sealed class SettingRoute(val route: String) {
    data object DeveloperOptions : SettingRoute("developer")
    data object Theme : SettingRoute("theme")
    data object Oss : SettingRoute("oss")
    data object Lab : SettingRoute("lab")
}

sealed class SchemeRoute(val route: String) {
    data object Share : SchemeRoute("share")
}
