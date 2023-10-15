package com.keelim.mygrade.ui.screen.grade

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavController.navigateGrade(
    subject: String,
    grade: String,
    point: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("profile?subject=$subject&grade=$grade&point=$point", navOptions)
}

fun NavGraphBuilder.gradeScreen(
    onCopyClick: () -> Unit,
    onShareClick: () -> Unit,
) {
    composable(
        route = "profile?subject={subject}&grade={grade}&point={point}",
        arguments =
        listOf(
            navArgument("subject") { defaultValue = "" },
            navArgument("grade") { defaultValue = "" },
            navArgument("point") { defaultValue = "" },
        ),
    ) {
        GradeRoute(
            onCopyClick = onCopyClick,
            onShareClick = onShareClick,
        )
    }
}
