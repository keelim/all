package com.keelim.comssa.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.keelim.composeutil.AppState
import com.keelim.comssa.ui.screen.main.calendar.calendarRoute
import com.keelim.comssa.ui.screen.main.calendar.calendarScreen
import com.keelim.comssa.ui.screen.main.flash.flashScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComssaHost(
    appState: AppState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = calendarRoute,
        modifier = modifier,
    ) {
        calendarScreen()
        flashScreen()
    }
}
