package com.keelim.comssa.ui.screen.main.calendar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.ComssaRoute

fun NavController.navigateCalendar(navOptions: NavOptions? = null) {
    this.navigate(ComssaRoute.Calendar, navOptions)
}

fun NavGraphBuilder.calendarScreen() {
    composable<ComssaRoute.Calendar> {
        CalendarRoute()
    }
}
