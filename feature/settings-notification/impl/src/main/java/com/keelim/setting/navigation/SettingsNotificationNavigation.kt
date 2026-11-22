package com.keelim.setting.navigation

import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.setting.screen.notification.NotificationRoute

fun EntryProviderScope<AppRoute>.registerSettingsNotificationEntries() {
    entry<SettingsNotificationDestination> {
        NotificationRoute()
    }
}
