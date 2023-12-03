package com.keelim.comssa.ui.screen.main.calendar

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val calendarRoute = "calendar"

fun NavController.navigateCalendar(navOptions: NavOptions? = null) {
  this.navigate(calendarRoute, navOptions)
}

fun NavGraphBuilder.calendarScreen() {
  composable(
      route = calendarRoute,
  ) {
    CalendarRoute()
  }
}
