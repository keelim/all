package com.keelim.setting.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.common.web.BrowserLauncher
import com.keelim.core.navigation.AppRoute
import com.keelim.setting.screen.settings.SettingsRoute

fun EntryProviderScope<AppRoute>.registerSettingsCoreEntries(
    backStack: SnapshotStateList<AppRoute>,
    context: Context,
    onOpenSourceClick: () -> Unit,
    browserLauncher: BrowserLauncher,
) {
    entry<SettingsHomeDestination> {
        SettingsRoute(
            onThemeChangeClick = { backStack.add(SettingsThemeDestination) },
            onNotificationsClick = {
                backStack.add(SettingsNotificationDestination)
            },
            onAlarmsClick = {
                backStack.add(SettingsAlarmDestination)
            },
            onFaqClick = {
                browserLauncher.open("https://keelim-vercel.vercel.app/faq")
            },
            onOpenSourceClick = onOpenSourceClick,
            onLabClick = {
                backStack.add(SettingsLabDestination)
            },
            onAppUpdateClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri(),
                    ),
                )
            },
            onAdminClick = {
                backStack.add(SettingsAdminDestination)
            },
            onDeviceInfoClick = {
                backStack.add(SettingsDeviceDestination)
            },
            browserLauncher = browserLauncher,
        )
    }
}
