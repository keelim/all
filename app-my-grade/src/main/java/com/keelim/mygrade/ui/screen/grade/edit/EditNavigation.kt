package com.keelim.mygrade.ui.screen.grade.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.MyGradeRoute

fun NavController.navigateEdit(
    subject: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(MyGradeRoute.Edit(subject = subject), navOptions)
}

fun NavGraphBuilder.editScreen() {
    composable<MyGradeRoute.Edit> {
        EditRoute()
    }
}
