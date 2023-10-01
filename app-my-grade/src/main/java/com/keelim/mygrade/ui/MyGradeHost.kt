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
import com.keelim.mygrade.ui.screen.quick.navigateQuick
import com.keelim.mygrade.ui.screen.quick.quickScreen
import com.keelim.mygrade.ui.screen.task.show.navigateTask
import com.keelim.mygrade.ui.screen.task.show.taskScreen
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
      onFloatingButtonClick2 = { navController.navigateQuick() },
      onFloatingButtonClick3 = { navController.navigateSettings() },
      onLabClick = {
        coroutineScope.launch {
          val result = onShowSnackbar("새로운 기능으로 준비중입니다 😀", "move")
          if (result) {
            navController.navigateTask()
          }
        }
      }
    )
    quickScreen(
      onDismiss = { navController.popBackStack() },
      onNavigate = { subject, normalProbability, student ->
        navController.navigateGrade(
          subject = subject,
          grade = normalProbability.grade(),
          point = Level((normalProbability.value * student) / 100).toProcess(student.toString()),
        )
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
    gradeScreen()
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
      onNavigateTaskClick = {},
    )
  }
}
