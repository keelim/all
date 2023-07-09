package com.keelim.mygrade.ui.screen.grade

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavController.navigateGrade(grade: String, point: String, navOptions: NavOptions? = null) {
    this.navigate("profile?grade=$grade&point=$point", navOptions)
}

fun NavGraphBuilder.gradeScreen() {
    composable(
        route = "profile?grade={grade}&point={point}",
        arguments = listOf(
            navArgument("grade") { defaultValue = "" },
            navArgument("point") { defaultValue = "" },
        ),
    ) {
        GradeRoute(
            onCopyClick = {},
            onShareClick = {},
        )
    }
}
