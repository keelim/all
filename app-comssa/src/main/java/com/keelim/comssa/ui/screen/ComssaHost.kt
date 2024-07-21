package com.keelim.comssa.ui.screen

import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.keelim.composeutil.AppState
import com.keelim.comssa.ui.screen.main.ecocal.ecocalScreen
import com.keelim.core.navigation.ComssaRoute
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
        startDestination = ComssaRoute.Ecocal,
        modifier = modifier,
    ) {
        ecocalScreen()
    }
}
