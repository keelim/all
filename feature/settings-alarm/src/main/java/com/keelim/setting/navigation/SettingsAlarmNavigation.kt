package com.keelim.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.alarm.AlarmRoute

fun EntryProviderScope<AppRoute>.registerSettingsAlarmEntries() {
    entry<FeatureRoute.Alarm> {
        AlarmRoute()
    }
}
