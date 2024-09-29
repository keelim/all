package com.keelim.setting.screen.alarm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute
import com.keelim.setting.screen.event.EventRoute

fun NavController.navigateAlarm(
    eventId: Int = 0,
    navOptions: NavOptions? = null,
) {
    this.navigate(FeatureRoute.Alarm)
}

fun NavGraphBuilder.alarmScreen() {
    composable<FeatureRoute.Alarm> {
        EventRoute()
    }
}
