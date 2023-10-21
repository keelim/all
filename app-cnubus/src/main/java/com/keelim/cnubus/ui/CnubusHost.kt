package com.keelim.cnubus.ui

import android.content.Intent
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.composeutil.AppState
import com.keelim.setting.screen.event.eventScreen
import com.keelim.setting.screen.navigateNotification
import com.keelim.setting.screen.notificationScreen
import com.keelim.setting.screen.settingsScreen
import com.keelim.setting.screen.welcome.welcomeScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun CnubusHost(
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
        startDestination = "main",
        modifier = modifier,
    ) {
        settingsScreen(
            onNotificationsClick = {
                navController.navigateNotification()
            },
            onOpenSourceClick = {
                context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
            },
            nestedGraphs = {
                notificationScreen()
            },
        )
        eventScreen()
        welcomeScreen {

        }
    }
}
