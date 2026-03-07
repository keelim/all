package com.keelim.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.theme.ThemeRoute

fun EntryProviderScope<AppRoute>.registerSettingsThemeEntries() {
    entry<FeatureRoute.Theme> {
        ThemeRoute()
    }
}
