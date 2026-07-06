package com.keelim.mygrade.ui.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import com.keelim.core.designsystem.component.KuiScaffold
import androidx.compose.material3.SnackbarDuration
import com.keelim.core.designsystem.component.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.keelim.mygrade.ui.MyGradeHost

@Composable
fun MyGradeApp(
    windowSizeClass: WindowSizeClass,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    KuiScaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
    ) { padding ->
        MyGradeHost(
            modifier = Modifier.padding(padding),
            coroutineScope = coroutineScope,
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
