package com.keelim.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.lab.LabRoute

fun EntryProviderScope<AppRoute>.registerSettingsLabEntries() {
    entry<FeatureRoute.Lab> {
        LabRoute()
    }
}
