package com.keelim.nandadiagnosis.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.core.navigation.NandaRoute
import com.keelim.nandadiagnosis.ui.screen.category.CategoriesType
import com.keelim.nandadiagnosis.ui.screen.category.categoryScreen
import com.keelim.nandadiagnosis.ui.screen.category.navigateToCategory
import com.keelim.nandadiagnosis.ui.screen.diagnosis.diagnosisScreen
import com.keelim.nandadiagnosis.ui.screen.diagnosis.navigateToDiagnosis
import com.keelim.nandadiagnosis.ui.screen.exercise.exerciseScreen
import com.keelim.nandadiagnosis.ui.screen.exercise.navigateToExercise
import com.keelim.nandadiagnosis.ui.screen.food.foodScreen
import com.keelim.nandadiagnosis.ui.screen.food.navigateToFood
import com.keelim.nandadiagnosis.ui.screen.inappweb.navigateToWeb
import com.keelim.nandadiagnosis.ui.screen.inappweb.webScreen
import com.keelim.nandadiagnosis.ui.screen.nutrient.nutrientScreen
import com.keelim.nandadiagnosis.ui.screen.nutrient.timer.navigateNutrientTimer
import com.keelim.nandadiagnosis.ui.screen.nutrient.timer.nutrientTimerScreen
import com.keelim.setting.screen.alarm.alarmScreen
import com.keelim.setting.screen.alarm.navigateAlarm
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.faq.faqScreen
import com.keelim.setting.screen.faq.navigateFaq
import com.keelim.setting.screen.lab.labScreen
import com.keelim.setting.screen.lab.navigateLab
import com.keelim.setting.screen.notification.navigateNotification
import com.keelim.setting.screen.notification.notificationScreen
import com.keelim.setting.screen.settings.navigateSettings
import com.keelim.setting.screen.settings.settingsScreen
import com.keelim.setting.screen.theme.navigateTheme
import com.keelim.setting.screen.theme.themeScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NandaHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = NandaRoute.Category,
        modifier = modifier,
    ) {
        webScreen(
            onNavigateCategory = navController::navigateToCategory,
        )
        categoryScreen(
            bottomSheetState = bottomSheetState,
            onBlogClick = {
                coroutineScope.launch { bottomSheetState.hide() }
                navController.navigateToWeb("nanda")
            },
            onAboutClick = {
                coroutineScope.launch { bottomSheetState.hide() }
                navController.navigateSettings()
            },
            onCategoryClick = { index, category -> navController.navigateToDiagnosis(index.toString(), category) },
            onEditTypeClick = { type ->
                when (type) {
                    CategoriesType.EXERCISE -> navController.navigateToExercise()
                    CategoriesType.FOOD -> navController.navigateToFood()
                    else -> {
                        coroutineScope.launch {
                            onShowSnackbar("현재 업데이트 준비중입니다. ", null)
                        }
                    }
                }
            },
            onDismiss = { coroutineScope.launch { bottomSheetState.hide() } },
            nestedGraphs = { diagnosisScreen() },
        )
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
            nestedGraphs = {
                faqScreen { }
                themeScreen()
                notificationScreen()
                labScreen()
                eventScreen()
                alarmScreen()
            },
        )
        eventScreen()
        nutrientScreen(
            onNutrientClick = { title, uri ->
                coroutineScope.launch {
                    val result = onShowSnackbar("$title 로 이동하시겠습니까?", "move")
                    if (result) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
                    }
                }
            },
            onNutrientTimerClick = navController::navigateNutrientTimer,
        )
        nutrientTimerScreen()
        exerciseScreen()
        foodScreen()
    }
}
