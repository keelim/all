package com.keelim.mygrade.ui.screen.grade

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateGrade(
    subject: String,
    grade: String,
    point: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        MyGradeRoute.Grade(
            subject = subject,
            grade = grade,
            point = point,
        ),
        navOptions,
    )
}

fun NavGraphBuilder.gradeScreen(
    onNavigateNotes: () -> Unit,
    onEditClick: (String) -> Unit,
    onShareClick: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    composable<MyGradeRoute.Grade> {
        GradeRoute(
            onNavigateNotes = onNavigateNotes,
            onEditClick = onEditClick,
            onShareClick = onShareClick,
        )
    }
    nestedGraphs()
}
