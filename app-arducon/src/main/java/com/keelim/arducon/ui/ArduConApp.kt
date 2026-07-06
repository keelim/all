package com.keelim.arducon.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import com.keelim.core.designsystem.component.KuiScaffold
import androidx.compose.material3.SnackbarDuration
import com.keelim.core.designsystem.component.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.keelim.arducon.ui.component.AdBannerView

@Composable
fun ArduconApp(
    windowSizeClass: WindowSizeClass,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    KuiScaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
        bottomBar = {
            AdBannerView(
                modifier = Modifier
                    .fillMaxWidth(),
            )
        },
    ) { padding ->
        ArduConHost(
            coroutineScope = coroutineScope,
            bottomSheetState = bottomSheetState,
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short,
                ) == SnackbarResult.ActionPerformed
            },
            modifier = Modifier
                .padding(padding),
        )
    }
}
