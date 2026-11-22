package com.keelim.setting.navigation

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.common.web.BrowserLauncher
import com.keelim.core.navigation.AppRoute
import com.keelim.setting.screen.event.EventRoute
import com.keelim.web.asCustomTabsBrowserLauncher

fun EntryProviderScope<AppRoute>.registerSettingsEntries(
    backStack: SnapshotStateList<AppRoute>,
    context: Context,
    onOpenSourceClick: () -> Unit,
    browserLauncher: BrowserLauncher = context.asCustomTabsBrowserLauncher(),
) {
    registerSettingsCoreEntries(
        backStack = backStack,
        context = context,
        onOpenSourceClick = onOpenSourceClick,
        browserLauncher = browserLauncher,
    )
    registerSettingsThemeEntries()
    registerSettingsNotificationEntries()
    registerSettingsLabEntries()
    registerSettingsAlarmEntries()
    registerSettingsAdminEntries()
    registerSettingsDeviceEntries(backStack = backStack)
}

fun EntryProviderScope<AppRoute>.registerSettingsEventEntry() {
    entry<SettingsEventDestination> {
        EventRoute()
    }
}
