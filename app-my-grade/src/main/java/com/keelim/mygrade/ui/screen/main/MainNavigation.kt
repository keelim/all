
package com.keelim.mygrade.ui.screen.main

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import org.apache.commons.math3.distribution.NormalDistribution

const val mainRoute = "main"
fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    this.navigate(mainRoute, navOptions)
}
fun NavGraphBuilder.mainScreen(
    onSubmitClick: (NormalProbability, Int) -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
) {
    composable(route = mainRoute) {
        MainRoute(
            onSubmitClick = onSubmitClick,
            onFloatingButtonClick1 = onFloatingButtonClick1,
            onFloatingButtonClick2 = onFloatingButtonClick2,
        )
    }
}

@JvmInline
value class Level(val level: Int)

internal fun Level.toProcess(count: String): String = "$level / $count"

@JvmInline
value class Zvalue(val z: Double)

internal fun Zvalue.getNormalProbability(): NormalProbability {
    return NormalDistribution()
        .let { normal ->
            if (z > 0) {
                NormalProbability(100 - ((normal.probability(0.0, z) + 0.5) * 100).toInt())
            } else {
                NormalProbability(((normal.probability(0.0, -z) + 0.5) * 100).toInt())
            }
        }
}

@JvmInline
value class NormalProbability(val value: Int)

internal fun NormalProbability.grade(isHardMode: Boolean = false): String {
    return when {
        value < 30 -> "A"
        value < 60 -> "B"
        value < 80 -> "C"
        value < 100 -> "D"
        else -> "F"
    }
}

sealed class MainState {
    object UnInitialized : MainState()
    object Loading : MainState()
    object Initialized : MainState()
    data class Success(
        val flag: Boolean,
        val value: NormalProbability,
        val student: Int = 0,
    ) : MainState()

    data class Error(val message: String) : MainState()
}
