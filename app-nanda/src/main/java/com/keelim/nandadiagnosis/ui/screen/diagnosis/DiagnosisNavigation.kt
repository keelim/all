package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

const val diagnosisRoute = "diagnosis"

fun NavController.navigateToDiagnosis(num: String, navOptions: NavOptions? = null) {
    this.navigate("$diagnosisRoute/$num", navOptions)
}

fun NavGraphBuilder.diagnosisScreen() {
    composable(
        route = "$diagnosisRoute/{num}",
        arguments = listOf(
            navArgument("num") { type = NavType.StringType }
        )
    ) {
        DiagnosisRoute(
            onDiagnosisClick = {}
        )
    }
}
