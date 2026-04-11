package com.keelim.setting.navigation

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute

fun EntryProviderScope<AppRoute>.registerSettingsEntries(
    backStack: SnapshotStateList<AppRoute>,
    context: Context,
    onOpenSourceClick: () -> Unit,
) {
    registerSettingsCoreEntries(
        backStack = backStack,
        context = context,
        onOpenSourceClick = onOpenSourceClick,
    )
    registerSettingsThemeEntries()
    registerSettingsNotificationEntries()
    registerSettingsLabEntries()
    registerSettingsAlarmEntries()
    registerSettingsAdminEntries()
    registerSettingsDeviceEntries(backStack = backStack)
}
