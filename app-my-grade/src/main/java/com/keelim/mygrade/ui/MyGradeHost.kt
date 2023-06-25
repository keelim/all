package com.keelim.mygrade.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.keelim.mygrade.ui.screen.main.Level
import com.keelim.mygrade.ui.screen.main.grade
import com.keelim.mygrade.ui.screen.main.mainRoute
import com.keelim.mygrade.ui.screen.main.mainScreen
import com.keelim.mygrade.ui.screen.quick.navigateQuick
import com.keelim.mygrade.ui.screen.quick.quickScreen
import com.keelim.mygrade.ui.screen.main.toProcess
import com.keelim.mygrade.ui.screen.grade.gradeScreen
import com.keelim.mygrade.ui.screen.grade.navigateGrade
import com.keelim.mygrade.ui.screen.history.historyScreen
import com.keelim.mygrade.ui.screen.history.navigateHistory

@Composable
fun MyGradeHost(
    appState: MyGradeState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = mainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onSubmitClick = { normalProbability, student ->
                navController.navigateGrade(
                    normalProbability.grade(),
                    Level((normalProbability.value * student) / 100).toProcess(student.toString())
                )
            },
            onFloatingButtonClick1 = { navController.navigateHistory() },
            onFloatingButtonClick2 = { navController.navigateQuick() }
        )
        quickScreen(
            onDismiss = {
                navController.popBackStack()
            },
            onNavigate = { normalProbability, student ->
                navController.navigateGrade(
                    normalProbability.grade(),
                    Level((normalProbability.value * student) / 100).toProcess(student.toString())
                )
            }
        )
        historyScreen(
            onHistoryClick = { normalProbability, student ->
                navController.navigateGrade(
                    normalProbability.grade(),
                    Level((normalProbability.value * student) / 100).toProcess(student.toString())
                )
            }
        )
        gradeScreen()
    }
}
