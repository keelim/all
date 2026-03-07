package com.keelim.setting.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.device.DeviceInfoScreen

fun EntryProviderScope<AppRoute>.registerSettingsDeviceEntries(
    backStack: SnapshotStateList<AppRoute>,
) {
    entry<FeatureRoute.DeviceInfo> {
        DeviceInfoScreen(
            onNavigateBack = { backStack.removeLastOrNull() },
        )
    }
}
