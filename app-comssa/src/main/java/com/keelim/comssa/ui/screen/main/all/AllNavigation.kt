package com.keelim.comssa.ui.screen.main.all

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val allRoute = "year"

fun NavController.navigateAll(navOptions: NavOptions? = null) {
  this.navigate(allRoute, navOptions)
}

fun NavGraphBuilder.allScreen() {
  composable(
      route = allRoute,
  ) {

  }
}
