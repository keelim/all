@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mygrade.ui

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import com.keelim.composeutil.navigation.KeelimNavDisplay
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.MyGradeRoute
import com.keelim.mygrade.ui.screen.analytics.StudyAnalyticsRoute
import com.keelim.mygrade.ui.screen.grade.GradeRoute
import com.keelim.mygrade.ui.screen.grade.edit.EditRoute
import com.keelim.mygrade.ui.screen.grade.notes.NotesRoute
import com.keelim.mygrade.ui.screen.history.HistoryRoute
import com.keelim.mygrade.ui.screen.main.Level
import com.keelim.mygrade.ui.screen.main.MainRoute
import com.keelim.mygrade.ui.screen.main.grade
import com.keelim.mygrade.ui.screen.main.toProcess
import com.keelim.mygrade.ui.screen.task.TaskRoute
import com.keelim.mygrade.ui.screen.task.chart.TaskChartRoute
import com.keelim.mygrade.ui.screen.timer.history.TimerHistoryRoute
import com.keelim.mygrade.ui.screen.word.show.WordShowRoute
import com.keelim.mygrade.ui.screen.word.write.WordWriteRoute
import com.keelim.setting.screen.event.EventRoute
import com.keelim.setting.navigation.registerSettingsEntries
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MyGradeHost(
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val backStack = rememberMutableStateListOf<AppRoute>(MyGradeRoute.Main())

    KeelimNavDisplay(
        modifier = modifier,
        backStack = backStack,
    ) {
            registerSettingsEntries(
                context = context,
                backStack = backStack,
                onOpenSourceClick = {
                    context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
                },
            )
            entry<FeatureRoute.Event> {
                EventRoute()
            }
            entry<MyGradeRoute.Main> { route ->
                MainRoute(
                    onSubmitClick = { subject, normalProbability, student ->
                        backStack.add(
                            MyGradeRoute.Grade(
                                subject = subject,
                                grade = normalProbability.grade(),
                                point = Level((normalProbability.value * student) / 100).toProcess(
                                    student.toString(),
                                ),
                            ),
                        )
                    },
                    onFloatingButtonClick1 = {
                        backStack.add(MyGradeRoute.History)
                    },
                    onFloatingButtonClick2 = {
                        backStack.add(FeatureRoute.Settings)
                    },
                    onLabClick = {
                        coroutineScope.launch {
                            onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                        }
                    },
                    onNavigateTimerHistory = {
                        backStack.add(MyGradeRoute.TimerHistory)
                    },
                    onNavigateTask = {
                        backStack.add(MyGradeRoute.Task)
                    },
                    onNavigateAnalytics = {
                        backStack.add(MyGradeRoute.StudyAnalytics)
                    },
                    timerPresetHours = route.timerHours.takeIf { it >= 0 },
                    timerPresetMinutes = route.timerMinutes.takeIf { it >= 0 },
                    timerPresetSeconds = route.timerSeconds.takeIf { it >= 0 },
                )
            }
            entry<MyGradeRoute.TimerHistory> {
                TimerHistoryRoute(
                    onSetTimer = { hours, minutes, seconds ->
                        backStack.removeLastOrNull()
                        backStack.add(
                            MyGradeRoute.Main(
                                timerHours = hours,
                                timerMinutes = minutes,
                                timerSeconds = seconds,
                            ),
                        )
                    },
                )
            }
            entry<MyGradeRoute.History> {
                HistoryRoute(
                    onHistoryClick = { subject, grade, point ->
                        backStack.add(
                            MyGradeRoute.Grade(
                                subject = subject,
                                grade = grade,
                                point = point,
                            ),
                        )
                    },
                )
            }
            entry<MyGradeRoute.Grade> {
                GradeRoute(
                    onNavigateNotes = {
                        backStack.add(MyGradeRoute.Notes)
                    },
                    onEditClick = { subject ->
                        backStack.add(
                            MyGradeRoute.Edit(subject = subject),
                        )
                    },
                    onShareClick = {
                        coroutineScope.launch {
                            onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                        }
                    },
                )
            }
            entry<MyGradeRoute.Edit> {
                EditRoute()
            }
            entry<MyGradeRoute.Notes> {
                NotesRoute()
            }
            entry<MyGradeRoute.Task> {
                TaskRoute(
                    onNavigateChart = {
                        backStack.add(MyGradeRoute.TaskChart)
                    },
                )
            }
            entry<MyGradeRoute.TaskChart> {
                TaskChartRoute()
            }
            entry<MyGradeRoute.Word> {
                WordShowRoute(
                    onWordWriteNavigate = {
                        backStack.add(MyGradeRoute.WordWrite)
                    },
                )
            }
            entry<MyGradeRoute.WordWrite> {
                WordWriteRoute()
            }
            entry<MyGradeRoute.StudyAnalytics> {
                StudyAnalyticsRoute()
            }
    }
}
