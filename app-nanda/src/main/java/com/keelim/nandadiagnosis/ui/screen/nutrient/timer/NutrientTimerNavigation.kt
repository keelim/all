package com.keelim.nandadiagnosis.ui.screen.nutrient.timer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateNutrientTimer(navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.NutrientTimer, navOptions)
}

fun NavGraphBuilder.nutrientTimerScreen() {
    composable<NandaRoute.NutrientTimer> {
        NutrientTimerRoute()
    }
}
