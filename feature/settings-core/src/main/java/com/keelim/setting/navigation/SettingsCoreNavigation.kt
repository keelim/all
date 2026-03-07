package com.keelim.setting.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.settings.SettingsRoute
import com.keelim.web.navigateToWebModule

fun EntryProviderScope<AppRoute>.registerSettingsCoreEntries(
    backStack: SnapshotStateList<AppRoute>,
    context: Context,
    onOpenSourceClick: () -> Unit,
) {
    entry<FeatureRoute.Settings> {
        SettingsRoute(
            onThemeChangeClick = { backStack.add(FeatureRoute.Theme) },
            onNotificationsClick = {
                backStack.add(FeatureRoute.Notification)
            },
            onAlarmsClick = {
                backStack.add(FeatureRoute.Alarm)
            },
            onFaqClick = {
                context.navigateToWebModule("https://keelim-vercel.vercel.app/faq".toUri())
            },
            onOpenSourceClick = onOpenSourceClick,
            onLabClick = {
                backStack.add(FeatureRoute.Lab)
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
                backStack.add(FeatureRoute.Admin)
            },
            onDeviceInfoClick = {
                backStack.add(FeatureRoute.DeviceInfo)
            },
        )
    }
}
