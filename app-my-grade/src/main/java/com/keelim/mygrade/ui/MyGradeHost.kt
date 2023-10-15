package com.keelim.mygrade.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.mygrade.ui.screen.grade.gradeScreen
import com.keelim.mygrade.ui.screen.grade.navigateGrade
import com.keelim.mygrade.ui.screen.history.historyScreen
import com.keelim.mygrade.ui.screen.history.navigateHistory
import com.keelim.mygrade.ui.screen.main.Level
import com.keelim.mygrade.ui.screen.main.grade
import com.keelim.mygrade.ui.screen.main.mainRoute
import com.keelim.mygrade.ui.screen.main.mainScreen
import com.keelim.mygrade.ui.screen.main.toProcess
import com.keelim.mygrade.ui.screen.task.add.navigateTaskAdd
import com.keelim.mygrade.ui.screen.task.add.taskAddScreen
import com.keelim.mygrade.ui.screen.task.show.navigateTask
import com.keelim.mygrade.ui.screen.task.show.navigateTaskPopUpTo
import com.keelim.mygrade.ui.screen.task.show.taskScreen
import com.keelim.mygrade.ui.screen.timer.history.navigateTimerHistory
import com.keelim.mygrade.ui.screen.timer.history.timerHistoryScreen
import com.keelim.mygrade.ui.screen.timer.timerScreen
import com.keelim.mygrade.ui.screen.word.navigateWord
import com.keelim.mygrade.ui.screen.word.navigateWordWrite
import com.keelim.mygrade.ui.screen.word.wordScreen
import com.keelim.mygrade.ui.screen.word.wordWriteScreen
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.navigateNotification
import com.keelim.setting.screen.navigateSettings
import com.keelim.setting.screen.notificationScreen
import com.keelim.setting.screen.settingsScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyGradeHost(
    appState: AppState,
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
            onSubmitClick = { subject, normalProbability, student ->
                navController.navigateGrade(
                    subject = subject,
                    grade = normalProbability.grade(),
                    point = Level((normalProbability.value * student) / 100).toProcess(student.toString()),
                )
            },
            onFloatingButtonClick1 = { navController.navigateHistory() },
            onFloatingButtonClick2 = { navController.navigateSettings() },
            onLabClick = {
                coroutineScope.launch {
                    val result = onShowSnackbar("새로운 기능으로 준비중입니다 😀", "move")
                    if (result) {
                        navController.navigateTask()
                    }
                }
            },
            onNavigateTimerHistory = navController::navigateTimerHistory,
            onNavigateWord = navController::navigateWord,
            nestedGraphs = {
                timerHistoryScreen()
            },
        )
        historyScreen(
            onHistoryClick = { subject, grade, point ->
                navController.navigateGrade(
                    subject = subject,
                    grade = grade,
                    point = point,
                )
            },
        )
        gradeScreen(
            onCopyClick = {
                coroutineScope.launch {
                    onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                }
            },
            onShareClick = {
                coroutineScope.launch {
                    onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                }
            }
        )
        settingsScreen(
            onNotificationsClick = { navController.navigateNotification() },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            nestedGraphs = { notificationScreen() },
        )
        eventScreen()
        taskScreen(
            onTaskClick = {},
            onNavigateTaskClick = navController::navigateTaskAdd,
        )
        taskAddScreen {
            navController.navigateTaskPopUpTo()
        }
        timerScreen()
        wordScreen(
            onWordWriteNavigate = navController::navigateWordWrite
        ) {
            wordWriteScreen()
        }
    }
}
