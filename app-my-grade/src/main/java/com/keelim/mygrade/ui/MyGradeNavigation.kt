package com.keelim.mygrade.ui

import androidx.navigation.NavHostController

private object MyGradeScreens {
    const val TODO_SCREEN = "todo"
}

object MyGradeDestinationsArgs {
}

object MyGradeDestinations {
    const val TODO_ROUTE = MyGradeScreens.TODO_SCREEN
}

class TodoNavigationActions(private val navController: NavHostController) {
    fun navigateToTodoScreen() {
        navController.navigate(MyGradeScreens.TODO_SCREEN)
    }
}
