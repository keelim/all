package com.keelim.mygrade.ui.screen.grade.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateNotes(
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.Notes, navOptions)
}

fun NavGraphBuilder.notesScreen() {
    composable<MyGradeRoute.Notes> {
        NotesRoute()
    }
}
