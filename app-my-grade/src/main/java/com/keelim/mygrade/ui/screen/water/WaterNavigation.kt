package com.keelim.mygrade.ui.screen.water

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateWater(navOptions: NavOptions? = null) {
    this.navigate(MyGradeRoute.Water, navOptions)
}

fun NavGraphBuilder.waterScreen() {
    composable<MyGradeRoute.Water> {
        WaterRoute()
    }
}
