package com.keelim.mygrade.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.core.navigation.MyGradeRoute
import com.keelim.mygrade.ui.screen.grade.edit.editScreen
import com.keelim.mygrade.ui.screen.grade.edit.navigateEdit
import com.keelim.mygrade.ui.screen.grade.gradeScreen
import com.keelim.mygrade.ui.screen.grade.navigateGrade
import com.keelim.mygrade.ui.screen.grade.notes.navigateNotes
import com.keelim.mygrade.ui.screen.grade.notes.notesScreen
import com.keelim.mygrade.ui.screen.history.historyScreen
import com.keelim.mygrade.ui.screen.history.navigateHistory
import com.keelim.mygrade.ui.screen.main.Level
import com.keelim.mygrade.ui.screen.main.grade
import com.keelim.mygrade.ui.screen.main.mainScreen
import com.keelim.mygrade.ui.screen.main.toProcess
import com.keelim.mygrade.ui.screen.task.chart.navigateTaskChart
import com.keelim.mygrade.ui.screen.task.chart.taskChartScreen
import com.keelim.mygrade.ui.screen.task.navigateTask
import com.keelim.mygrade.ui.screen.task.taskScreen
import com.keelim.mygrade.ui.screen.timer.history.navigateTimerHistory
import com.keelim.mygrade.ui.screen.timer.history.timerHistoryScreen
import com.keelim.mygrade.ui.screen.timer.timerScreen
import com.keelim.mygrade.ui.screen.word.navigateWordWrite
import com.keelim.mygrade.ui.screen.word.wordScreen
import com.keelim.mygrade.ui.screen.word.wordWriteScreen
import com.keelim.setting.screen.admin.navigateAdmin
import com.keelim.setting.screen.alarm.navigateAlarm
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.faq.navigateFaq
import com.keelim.setting.screen.lab.navigateLab
import com.keelim.setting.screen.notification.navigateNotification
import com.keelim.setting.screen.settings.navigateSettings
import com.keelim.setting.screen.settings.settingsScreen
import com.keelim.setting.screen.theme.navigateTheme
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
        startDestination = MyGradeRoute.Main,
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
                    val result = onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                    // if (result) {
                    //     navController.navigateTask()
                    // }
                }
            },
            onNavigateTimerHistory = navController::navigateTimerHistory,
            onNavigateTask = navController::navigateTask,
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
            onNavigateNotes = navController::navigateNotes,
            onEditClick = { subject ->
                navController.navigateEdit(
                    subject = subject,
                )
            },
            onShareClick = {
                coroutineScope.launch {
                    onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                }
            },
        ) {
            editScreen()
        }
        notesScreen()
        settingsScreen(
            onThemeChangeClick = navController::navigateTheme,
            onNotificationsClick = navController::navigateNotification,
            onAlarmsClick = navController::navigateAlarm,
            onFaqClick = navController::navigateFaq,
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            onLabClick = navController::navigateLab,
            onAppUpdateClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}"),
                    ),
                )
            },
            onAdminClick = navController::navigateAdmin,
        )
        eventScreen()
        taskScreen(
            onNavigateChart = navController::navigateTaskChart,
        ) {
            taskChartScreen()
        }
        timerScreen()
        wordScreen(
            onWordWriteNavigate = navController::navigateWordWrite,
        ) {
            wordWriteScreen()
        }
    }
}
