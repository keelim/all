package com.keelim.nandadiagnosis.ui.screen.food.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateToFoodEdit(title: String, navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.FoodEdit(title = title), navOptions)
}

fun NavGraphBuilder.foodEditScreen() {
    composable<NandaRoute.FoodEdit> {
        FoodEditRoute()
    }
}
