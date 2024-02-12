package com.keelim.mygrade.ui.screen.grade

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val gradeRoute = "grade"
fun NavController.navigateGrade(
    subject: String,
    grade: String,
    point: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("$gradeRoute?subject=$subject&grade=$grade&point=$point", navOptions)
}

fun NavGraphBuilder.gradeScreen(
    onNavigateNotes: () -> Unit,
    onEditClick: (String) -> Unit,
    onShareClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable(
        route = "$gradeRoute?subject={subject}&grade={grade}&point={point}",
        arguments =
        listOf(
            navArgument("subject") { defaultValue = "" },
            navArgument("grade") { defaultValue = "" },
            navArgument("point") { defaultValue = "" },
        ),
    ) {
        GradeRoute(
            onNavigateNotes = onNavigateNotes,
            onEditClick = onEditClick,
            onShareClick = onShareClick,
        )
    }
    nestedGraphs()
}
