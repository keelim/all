@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.mygrade.ui.screen.quick

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.keelim.mygrade.ui.screen.main.NormalProbability

const val quickRoute = "quick"

fun NavController.navigateQuick(navOptions: NavOptions? = null) {
    this.navigate(quickRoute, navOptions)
}

fun NavGraphBuilder.quickScreen(
    onDismiss: () -> Unit,
    onNavigate: (NormalProbability, Int) -> Unit,
) {
    composable(route = quickRoute) {
        ModalBottomSheet(onDismissRequest = { onDismiss() }) {
            QuickAddRoute(
                onNavigate = onNavigate,
            )
        }
    }
}
