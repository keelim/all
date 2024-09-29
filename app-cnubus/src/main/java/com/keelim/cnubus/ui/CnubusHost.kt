package com.keelim.cnubus.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.cnubus.ui.screen.main.mainScreen
import com.keelim.cnubus.ui.screen.map.screen.map.mapScreen
import com.keelim.cnubus.ui.screen.map.screen.map.navigateMap
import com.keelim.composeutil.AppState
import com.keelim.core.navigation.CnuBusRoute
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
        startDestination = CnuBusRoute.Main,
        modifier = modifier,
    ) {
        mainScreen(
            onNavigateMap = navController::navigateMap,
            onNavigateAppSetting = navController::navigateSettings,
            nestedGraphs = {
                mapScreen()
            },
        )
        settingsScreen(
            onThemeChangeClick = navController::navigateTheme,
            onNotificationsClick = navController::navigateNotification,
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
                faqScreen {
                }
                themeScreen()
                notificationScreen()
                labScreen()
            },
        )
        eventScreen()
        welcomeScreen {
        }
    }
}
