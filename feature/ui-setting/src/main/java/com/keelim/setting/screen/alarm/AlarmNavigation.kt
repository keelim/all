package com.keelim.setting.screen.alarm

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.FeatureRoute

fun NavController.navigateAlarm(
    navOptions: NavOptions? = null,
) {
    this.navigate(FeatureRoute.Alarm)
}

fun NavGraphBuilder.alarmScreen() {
    composable<FeatureRoute.Alarm> {
        AlarmRoute()
    }
}
