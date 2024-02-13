package com.keelim.mygrade.ui.screen.grade.notes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val notesRoute = "notes"
fun NavController.navigateNotes(
    navOptions: NavOptions? = null,
) {
    this.navigate(notesRoute, navOptions)
}

fun NavGraphBuilder.notesScreen() {
    composable(
        route = notesRoute,
    ) {
        NotesRoute()
    }
}
