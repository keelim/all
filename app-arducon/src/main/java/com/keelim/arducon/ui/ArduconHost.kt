package com.keelim.arducon.ui

import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.keelim.arducon.ui.screen.main.mainRoute
import com.keelim.arducon.ui.screen.main.mainScreen
import com.keelim.composeutil.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ArduConHost(
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
        startDestination = mainRoute,
        modifier = modifier,
    ) {
        mainScreen(
            onShowMessage = { message ->
                coroutineScope.launch {
                    onShowSnackbar(message, null)
                }
            },
            nestedGraphs = {},
        )
    }
}
