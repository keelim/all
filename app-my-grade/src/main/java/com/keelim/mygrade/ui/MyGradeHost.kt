package com.keelim.mygrade.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.mygrade.ui.screen.grade.gradeScreen
import com.keelim.mygrade.ui.screen.grade.navigateGrade
import com.keelim.mygrade.ui.screen.history.historyScreen
import com.keelim.mygrade.ui.screen.history.navigateHistory
import com.keelim.mygrade.ui.screen.main.Level
import com.keelim.mygrade.ui.screen.main.grade
import com.keelim.mygrade.ui.screen.main.mainRoute
import com.keelim.mygrade.ui.screen.main.mainScreen
import com.keelim.mygrade.ui.screen.main.toProcess
import com.keelim.mygrade.ui.screen.quick.navigateQuick
import com.keelim.mygrade.ui.screen.quick.quickScreen
import com.keelim.setting.screen.navigateSettings
import com.keelim.setting.screen.settingsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyGradeHost(
    appState: MyGradeState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = mainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onSubmitClick = { normalProbability, student ->
                navController.navigateGrade(
                    normalProbability.grade(),
                    Level((normalProbability.value * student) / 100).toProcess(student.toString()),
                )
            },
            onFloatingButtonClick1 = { navController.navigateHistory() },
            onFloatingButtonClick2 = { navController.navigateQuick() },
            onFloatingButtonClick3 = { navController.navigateSettings() }
        )
        quickScreen(
            onDismiss = {
                navController.popBackStack()
            },
            onNavigate = { normalProbability, student ->
                navController.navigateGrade(
                    normalProbability.grade(),
                    Level((normalProbability.value * student) / 100).toProcess(student.toString()),
                )
            },
        )
        historyScreen(
            onHistoryClick = { grade, point ->
                navController.navigateGrade(
                    grade, point
                )
            },
        )
        gradeScreen()
        settingsScreen(
            onNotificationsClick = {
                coroutineScope.launch {
                    onShowSnackbar("현재 준비 중입니다. ", null)
                }
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            }
        )
    }
}
