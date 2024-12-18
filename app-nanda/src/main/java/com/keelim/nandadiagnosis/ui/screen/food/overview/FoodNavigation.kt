package com.keelim.nandadiagnosis.ui.screen.food.overview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateToFood(navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.Food, navOptions)
}

fun NavGraphBuilder.foodScreen(
    onEditClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {}
) {
    composable<NandaRoute.Food> {
        FoodRoute(
            onEditClick = onEditClick,
        )
    }
    nestedGraphs()
}
