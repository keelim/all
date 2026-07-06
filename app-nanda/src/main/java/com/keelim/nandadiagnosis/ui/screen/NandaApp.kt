package com.keelim.nandadiagnosis.ui.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import com.keelim.core.designsystem.component.KuiCenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiModalDrawerSheet
import com.keelim.core.designsystem.component.KuiModalNavigationDrawer
import com.keelim.core.designsystem.component.KuiScaffold
import androidx.compose.material3.SnackbarDuration
import com.keelim.core.designsystem.component.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.keelim.composeutil.rememberMutableStateListOf
import com.keelim.composeutil.resource.space12
import com.keelim.core.navigation.AppRoute
import com.keelim.core.navigation.NandaRoute
import com.keelim.nandadiagnosis.ui.NandaHost
import com.keelim.nandadiagnosis.ui.screen.main.NandaDrawer
import kotlinx.coroutines.launch

@Composable
fun NandaApp(
    windowSizeClass: WindowSizeClass,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetState = rememberModalBottomSheetState()
    val backStack = rememberMutableStateListOf<AppRoute>(NandaRoute.Category)
    KuiModalNavigationDrawer(
        drawerContent = {
            Spacer(
                modifier = Modifier.height(space12),
            )
            KuiModalDrawerSheet {
                NandaDrawer(
                    onRouteClick = { route ->
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        backStack.add(route)
                    },
                    onAboutClick = {
                        coroutineScope.launch {
                            drawerState.close()
                            bottomSheetState.show()
                        }
                    },
                )
            }
        },
        drawerState = drawerState,
    ) {
        KuiScaffold(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            topBar = {
                KuiCenterAlignedTopAppBar(
                    title = { KuiText(text = "난다진다") },
                    navigationIcon = {
                        KuiIconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            KuiIcon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Drawer Icon",
                            )
                        }
                    },
                )
            },
            snackbarHost = { KuiSnackbarHost(snackbarHostState) },
        ) { padding ->
            NandaHost(
                modifier = Modifier.padding(padding),
                bottomSheetState = bottomSheetState,
                coroutineScope = coroutineScope,
                onShowSnackbar = { message, action ->
                    snackbarHostState.showSnackbar(
                        message = message,
                        actionLabel = action,
                        duration = SnackbarDuration.Short,
                    ) == SnackbarResult.ActionPerformed
                },
                backStack = backStack,
            )
        }
    }
}
