package com.keelim.setting.screen.settings

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.admin.AdminRoute
import com.keelim.setting.screen.alarm.AlarmRoute
import com.keelim.setting.screen.faq.FaqRoute
import com.keelim.setting.screen.lab.LabRoute
import com.keelim.setting.screen.notification.NotificationRoute
import com.keelim.setting.screen.theme.ThemeRoute

fun NavController.navigateSettings(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Settings, navOptions)
}

fun EntryProviderBuilder<Any>.settingsEntry(
    context: Context,
    backStack: SnapshotStateList<Any>,
) {
    entry<FeatureRoute.Settings>() {
        SettingsRoute(
            onThemeChangeClick = { backStack.add(FeatureRoute.Theme) },
            onNotificationsClick = {
                backStack.add(FeatureRoute.Notification)
            },
            onAlarmsClick = {
                backStack.add(FeatureRoute.Alarm)
            },
            onFaqClick = {
                backStack.add(FeatureRoute.Faq)
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
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
            }
        )
    }
    entry<FeatureRoute.Faq> {
        FaqRoute()
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
}
