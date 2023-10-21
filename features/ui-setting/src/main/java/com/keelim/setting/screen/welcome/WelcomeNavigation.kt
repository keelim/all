package com.keelim.setting.screen.welcome

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val welcomeRoute = "welcome"

fun NavController.navigateWelcome(navOptions: NavOptions? = null) {
  this.navigate(welcomeRoute, navOptions)
}

fun NavGraphBuilder.welcomeScreen(
    onNavigateMain: () -> Unit
) {
  composable(
      route = welcomeRoute,
  ) {
    WelcomeRoute(
        onNavigateMain = onNavigateMain
    )
  }
}
