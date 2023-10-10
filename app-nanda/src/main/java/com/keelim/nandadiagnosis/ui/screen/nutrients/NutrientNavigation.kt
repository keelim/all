package com.keelim.nandadiagnosis.ui.screen.nutrients

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val nutrientRoute = "nutrient"

fun NavController.navigateToNutrient(navOptions: NavOptions? = null) {
    this.navigate(nutrientRoute, navOptions)
}

fun NavGraphBuilder.nutrientScreen(
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
) {
    composable(route = nutrientRoute) {
        NutrientRoute(
            onNutrientClick = onNutrientClick,
            onNutrientTimerClick = onNutrientTimerClick,
        )
    }
}
