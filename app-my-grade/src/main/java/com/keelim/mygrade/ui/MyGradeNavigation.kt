package com.keelim.mygrade.ui

import androidx.navigation.NavHostController

private object MyGradeScreens {
    const val MAIN_SCREEN = "main"
    const val TODO_SCREEN = "todo"
}

object MyGradeDestinationsArgs

object MyGradeDestinations {
    const val MAIN_ROUTE = MyGradeScreens.MAIN_SCREEN
    const val TODO_ROUTE = MyGradeScreens.TODO_SCREEN
}

class TodoNavigationActions(private val navController: NavHostController) {
    fun navigateToMainScreen() = navController.navigate(MyGradeScreens.MAIN_SCREEN)
    fun navigateToTodoScreen()  =navController.navigate(MyGradeScreens.TODO_SCREEN)
}
