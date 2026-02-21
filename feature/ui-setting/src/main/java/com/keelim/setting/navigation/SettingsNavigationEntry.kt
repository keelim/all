package com.keelim.setting.navigation

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.navigation3.runtime.EntryProviderScope
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.admin.AdminRoute
import com.keelim.setting.screen.alarm.AlarmRoute
import com.keelim.setting.screen.device.DeviceInfoScreen
import com.keelim.setting.screen.lab.LabRoute
import com.keelim.setting.screen.notification.NotificationRoute
import com.keelim.setting.screen.settings.SettingsRoute
import com.keelim.setting.screen.theme.ThemeRoute
import com.keelim.web.navigateToWebModule

@Composable
fun EntryProviderScope<Any>.registerSettingsEntries(
    backStack: SnapshotStateList<Any>,
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

    entry<FeatureRoute.Theme> {
        ThemeRoute()
    }
    entry<FeatureRoute.Notification> {
        NotificationRoute()
    }
    entry<FeatureRoute.Lab> {
        LabRoute()
    }
    entry<FeatureRoute.Alarm> {
        AlarmRoute()
    }
    entry<FeatureRoute.Admin> {
        AdminRoute()
    }
    entry<FeatureRoute.DeviceInfo> {
        DeviceInfoScreen(
            onNavigateBack = { backStack.removeLastOrNull() },
        )
    }
}
