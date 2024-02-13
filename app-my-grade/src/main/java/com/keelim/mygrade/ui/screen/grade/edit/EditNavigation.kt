package com.keelim.mygrade.ui.screen.grade.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.keelim.mygrade.ui.screen.grade.gradeRoute

const val editRoute = "$gradeRoute/edit"
fun NavController.navigateEdit(
    subject: String,
    navOptions: NavOptions? = null,
) {
    this.navigate("$editRoute?subject=$subject", navOptions)
}

fun NavGraphBuilder.editScreen() {
    composable(
        route = "$editRoute?subject={subject}",
        arguments =
        listOf(
            navArgument("subject") { defaultValue = "" },
        ),
    ) {
        EditRoute()
    }
}
