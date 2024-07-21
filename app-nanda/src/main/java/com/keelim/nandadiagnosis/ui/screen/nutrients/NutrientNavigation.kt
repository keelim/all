package com.keelim.nandadiagnosis.ui.screen.nutrients

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateToNutrient(navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.Nutrient, navOptions)
}

fun NavGraphBuilder.nutrientScreen(
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
) {
    composable<NandaRoute.Nutrient> {
        NutrientRoute(
            onNutrientClick = onNutrientClick,
            onNutrientTimerClick = onNutrientTimerClick,
        )
    }
}
