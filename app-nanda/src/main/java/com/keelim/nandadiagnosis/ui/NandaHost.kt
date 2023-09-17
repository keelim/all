package com.keelim.nandadiagnosis.ui

import android.content.Intent
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.nandadiagnosis.ui.screen.category.categoryRoute
import com.keelim.nandadiagnosis.ui.screen.category.categoryScreen
import com.keelim.nandadiagnosis.ui.screen.diagnosis.diagnosisScreen
import com.keelim.nandadiagnosis.ui.screen.diagnosis.navigateToDiagnosis
import com.keelim.nandadiagnosis.ui.screen.inappweb.navigateToWeb
import com.keelim.nandadiagnosis.ui.screen.inappweb.webScreen
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.navigateNotification
import com.keelim.setting.screen.navigateSettings
import com.keelim.setting.screen.notificationScreen
import com.keelim.setting.screen.settingsScreen
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
        webScreen()
        categoryScreen(
            bottomSheetState = bottomSheetState,
            onBlogClick = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                navController.navigateToWeb("nanda")
            },
            onAboutClick = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
                navController.navigateSettings()
            },
            onCategoryClick = { index ->
                navController.navigateToDiagnosis(index.toString())
            },
            onDismiss = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                }
            },
            nestedGraphs = {
                diagnosisScreen()
            },
        )
        settingsScreen(
            onNotificationsClick = {
                navController.navigateNotification()
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            nestedGraphs = {
                notificationScreen()
            }
        )
        eventScreen()
    }
}
