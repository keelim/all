package com.keelim.nandadiagnosis.ui.screen.exercise

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateToExercise(navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.Exercise, navOptions)
}

fun NavGraphBuilder.exerciseScreen() {
    composable<NandaRoute.Exercise> {
        ExerciseRoute()
    }
}
