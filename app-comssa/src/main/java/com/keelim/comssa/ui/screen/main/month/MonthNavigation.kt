package com.keelim.comssa.ui.screen.main.month

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.comssa.ui.screen.main.calendar.CalendarRoute
import com.keelim.comssa.ui.screen.main.calendar.calendarRoute

const val monthRoute = "month"

fun NavController.navigateMonth(navOptions: NavOptions? = null) {
  this.navigate(monthRoute, navOptions)
}

fun NavGraphBuilder.monthScreen() {
  composable(
      route = monthRoute,
  ) {

  }
}
