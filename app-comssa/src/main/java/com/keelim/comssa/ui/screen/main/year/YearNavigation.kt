package com.keelim.comssa.ui.screen.main.year

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.comssa.ui.screen.main.calendar.CalendarRoute
import com.keelim.comssa.ui.screen.main.calendar.calendarRoute
import com.keelim.comssa.ui.screen.main.month.monthRoute

const val yearRoute = "year"

fun NavController.navigateYear(navOptions: NavOptions? = null) {
  this.navigate(yearRoute, navOptions)
}

fun NavGraphBuilder.yearScreen() {
  composable(
      route = yearRoute,
  ) {

  }
}
