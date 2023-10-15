package com.keelim.nandadiagnosis.ui.screen.nutrients.timer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val nutrientTimerRoute = "timer"

fun NavController.navigateNutrientTimer(navOptions: NavOptions? = null) {
  this.navigate(nutrientTimerRoute, navOptions)
}

fun NavGraphBuilder.nutrientTimerScreen() {
  composable(
    route = nutrientTimerRoute,
  ) {
    NutrientTimerRoute()
  }
}
