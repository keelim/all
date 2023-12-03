@file:OptIn(ExperimentalLayoutApi::class)

package com.keelim.comssa.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberAppState
import com.keelim.comssa.ui.screen.ComssaHost

@Composable
fun ComssaApp(
    windowSizeClass: WindowSizeClass,
    appState: AppState = rememberAppState(windowSizeClass = windowSizeClass),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Comssa")
                        },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Rounded.Menu,
                            contentDescription = "Drawer Icon",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                ComssaHost(
                    appState = appState,
                    bottomSheetState = bottomSheetState,
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
    }
}
