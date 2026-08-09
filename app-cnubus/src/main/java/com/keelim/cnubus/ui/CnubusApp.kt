package com.keelim.cnubus.ui

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

@Composable
fun CnubusApp(
    windowSizeClass: WindowSizeClass,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    KuiScaffold(
        modifier = Modifier.safeDrawingPadding(),
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
    ) { padding ->
        CnubusHost(
            modifier = Modifier.padding(padding),
            coroutineScope = coroutineScope,
            bottomSheetState = bottomSheetState,
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = SnackbarDuration.Short,
                ) == SnackbarResult.ActionPerformed
            },
        )
    }
}
