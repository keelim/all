package com.keelim.comssa.ui.screen

import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.keelim.composeutil.AppState
import com.keelim.comssa.ui.screen.main.ecocal.ecocalRoute
import com.keelim.comssa.ui.screen.main.ecocal.ecocalScreen
import com.keelim.comssa.ui.screen.main.calendar.calendarRoute
import com.keelim.comssa.ui.screen.main.calendar.calendarScreen
import com.keelim.comssa.ui.screen.main.flash.flashScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun ComssaHost(
    appState: AppState,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = ecocalRoute,
        modifier = modifier,
    ) {
        ecocalScreen()
    }
}
