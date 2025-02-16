package com.keelim.setting.screen.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.alarm.alarmScreen
import com.keelim.setting.screen.faq.faqScreen
import com.keelim.setting.screen.lab.labScreen
import com.keelim.setting.screen.notification.notificationScreen
import com.keelim.setting.screen.theme.themeScreen

fun NavController.navigateSettings(navOptions: NavOptions? = null) {
    this.navigate(FeatureRoute.Settings, navOptions)
}

fun NavGraphBuilder.settingsScreen(
    onThemeChangeClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onAlarmsClick: () -> Unit,
    onFaqClick: () -> Unit,
    onOpenSourceClick: () -> Unit,
    onLabClick: () -> Unit,
    onAppUpdateClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {
        faqScreen(nestedGraphs = {})
        themeScreen()
        notificationScreen()
        labScreen()
        alarmScreen()
    }
) {
    composable<FeatureRoute.Settings> {
        SettingsRoute(
            onThemeChangeClick = onThemeChangeClick,
            onNotificationsClick = onNotificationsClick,
            onAlarmsClick = onAlarmsClick,
            onFaqClick = onFaqClick,
            onOpenSourceClick = onOpenSourceClick,
            onLabClick = onLabClick,
            onAppUpdateClick = onAppUpdateClick,
        )
    }
    nestedGraphs()
}
