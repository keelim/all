package com.keelim.arducon.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.keelim.arducon.ui.screen.main.mainScreen
import com.keelim.arducon.ui.screen.qr.navigateQr
import com.keelim.arducon.ui.screen.qr.qrScreen
import com.keelim.arducon.ui.screen.search.navigateSearch
import com.keelim.arducon.ui.screen.search.searchScreen
import com.keelim.composeutil.AppState
import com.keelim.core.navigation.ArduconRoute
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
        startDestination = ArduconRoute.Main,
        modifier = modifier,
    ) {
        mainScreen(
            onShowMessage = { message ->
                coroutineScope.launch {
                    onShowSnackbar(message, null)
                }
            },
            onQrCodeClick = navController::navigateQr,
            onNavigateSearch = navController::navigateSearch,
            nestedGraphs = {
                qrScreen(
                    onShowBarcode = { barcode ->
                        coroutineScope.launch {
                            if (onShowSnackbar(barcode, null)) {
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(barcode),
                                ).let { context.startActivity(it) }
                            }
                        }
                    },
                )
                searchScreen()
            },
        )
    }
}
