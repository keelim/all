package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.core.navigation.NandaRoute

fun NavController.navigateToDiagnosis(num: String, category: String, navOptions: NavOptions? = null) {
    this.navigate(NandaRoute.Diagnosis(num, category), navOptions)
}

fun NavGraphBuilder.diagnosisScreen() {
    composable<NandaRoute.Diagnosis> {
        DiagnosisRoute(
            onDiagnosisClick = {},
        )
    }
}
