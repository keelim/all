@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mygrade.ui

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.FeatureRoute
import com.keelim.core.navigation.MyGradeRoute
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
import com.keelim.setting.screen.settings.settingsEntry
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
    val backStack = rememberMutableStateListOf<AppRoute>(MyGradeRoute.Main)
    val motionScheme = MaterialTheme.motionScheme

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                fadeOut(motionScheme.defaultEffectsSpec()),
            )
        },
        popTransitionSpec = {
            ContentTransform(
                fadeIn(motionScheme.defaultEffectsSpec()),
                scaleOut(
                    targetScale = 0.7f,
                ),
            )
        },
        entryProvider = entryProvider {
            settingsEntry(
                context = context,
                backStack = backStack,
            )
            entry<FeatureRoute.Event> {
                EventRoute()
            }
            entry<MyGradeRoute.Main> {
                MainRoute(
                    onSubmitClick = { subject, normalProbability, student ->
                        backStack.add(
                            MyGradeRoute.Grade(
                                subject = subject,
                                grade = normalProbability.grade(),
                                point = Level((normalProbability.value * student) / 100).toProcess(
                                    student.toString()
                                ),
                            )
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
                            val result = onShowSnackbar("새로운 기능으로 준비중입니다 😀", null)
                            // if (result) {
                            //     navController.navigateTask()
                            // }
                        }
                    },
                    onNavigateTimerHistory = {
                        backStack.add(MyGradeRoute.TimerHistory)
                    },
                    onNavigateTask = {
                        backStack.add(MyGradeRoute.Task)
                    },
                )
            }
            entry<MyGradeRoute.TimerHistory> {
                TimerHistoryRoute()
            }
            entry<MyGradeRoute.History> {
                HistoryRoute(
                    onHistoryClick = { subject, grade, point ->
                        backStack.add(
                            MyGradeRoute.Grade(
                                subject = subject,
                                grade = grade,
                                point = point,
                            )
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
                            MyGradeRoute.Edit(subject = subject)
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

        }
    )
}
