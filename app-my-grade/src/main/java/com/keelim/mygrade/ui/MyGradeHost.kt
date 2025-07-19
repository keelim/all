@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.mygrade.ui

import android.content.Context
import android.content.Intent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderBuilder
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
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
import com.keelim.setting.screen.admin.AdminRoute
import com.keelim.setting.screen.alarm.AlarmRoute
import com.keelim.setting.screen.event.EventRoute
import com.keelim.setting.screen.faq.FaqRoute
import com.keelim.setting.screen.lab.LabRoute
import com.keelim.setting.screen.notification.NotificationRoute
import com.keelim.setting.screen.settings.SettingsRoute
import com.keelim.setting.screen.theme.ThemeRoute
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
        },
    )
}

@Composable
fun EntryProviderBuilder<Any>.settingsEntry(
    backStack: SnapshotStateList<Any>,
    context: Context
) {
    entry<FeatureRoute.Settings> {
        SettingsRoute(
            onThemeChangeClick = { backStack.add(FeatureRoute.Theme) },
            onNotificationsClick = {
                backStack.add(FeatureRoute.Notification)
            },
            onAlarmsClick = {
                backStack.add(FeatureRoute.Alarm)
            },
            onFaqClick = {
                backStack.add(FeatureRoute.Faq)
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            onLabClick = {
                backStack.add(FeatureRoute.Lab)
            },
            onAppUpdateClick = {
                context.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri(),
                    ),
                )
            },
            onAdminClick = {
                backStack.add(FeatureRoute.Admin)
            },
        )
    }
    entry<FeatureRoute.Faq> {
        FaqRoute()
    }
    entry<FeatureRoute.Theme> {
        ThemeRoute()
    }
    entry<FeatureRoute.Notification> {
        NotificationRoute()
    }
    entry<FeatureRoute.Lab> {
        LabRoute()
    }
    entry<FeatureRoute.Alarm> {
        AlarmRoute()
    }
    entry<FeatureRoute.Admin> {
        AdminRoute()
    }
}

