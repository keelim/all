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
import com.keelim.nandadiagnosis.ui.screen.category.categoryRoute
import com.keelim.nandadiagnosis.ui.screen.category.categoryScreen
import com.keelim.nandadiagnosis.ui.screen.category.navigateToCategory
import com.keelim.nandadiagnosis.ui.screen.diagnosis.diagnosisScreen
import com.keelim.nandadiagnosis.ui.screen.diagnosis.navigateToDiagnosis
import com.keelim.nandadiagnosis.ui.screen.inappweb.navigateToWeb
import com.keelim.nandadiagnosis.ui.screen.inappweb.webScreen
import com.keelim.nandadiagnosis.ui.screen.nutrients.nutrientScreen
import com.keelim.nandadiagnosis.ui.screen.nutrients.timer.navigateNutrientTimer
import com.keelim.nandadiagnosis.ui.screen.nutrients.timer.nutrientTimerScreen
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.lab.labScreen
import com.keelim.setting.screen.lab.navigateLab
import com.keelim.setting.screen.navigateNotification
import com.keelim.setting.screen.notificationScreen
import com.keelim.setting.screen.settings.navigateSettings
import com.keelim.setting.screen.settings.settingsScreen
import com.keelim.setting.screen.welcome.welcomeScreen
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
        startDestination = categoryRoute,
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
            onCategoryClick = { index -> navController.navigateToDiagnosis(index.toString()) },
            onDismiss = { coroutineScope.launch { bottomSheetState.hide() } },
            nestedGraphs = { diagnosisScreen() },
        )
        settingsScreen(
            onNotificationsClick = navController::navigateNotification,
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            onLabClick = navController::navigateLab,
            nestedGraphs = {
                notificationScreen()
                labScreen()
                eventScreen()
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
        welcomeScreen(
            onNavigateMain = navController::navigateToCategory,
        )
    }
}
