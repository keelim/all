package com.keelim.nandadiagnosis.ui

import androidx.navigation.NavHostController

private object NandaScreens {
    const val MAIN_SCREEN = "main"
}

object NandaDestinationsArgs
object NandaDestinations {
    const val MAIN_ROUTE = NandaScreens.MAIN_SCREEN
}

class TodoNavigationActions(private val navController: NavHostController) {
    fun navigateToMainScreen() = navController.navigate(NandaScreens.MAIN_SCREEN)
}
