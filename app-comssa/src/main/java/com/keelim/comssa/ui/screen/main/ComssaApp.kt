@file:OptIn(ExperimentalMaterial3AdaptiveNavigationSuiteApi::class)

package com.keelim.comssa.ui.screen.main

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigation.suite.ExperimentalMaterial3AdaptiveNavigationSuiteApi
import androidx.compose.material3.adaptive.navigation.suite.NavigationSuiteScaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEachIndexed
import com.keelim.composeutil.AppState
import com.keelim.composeutil.rememberAppState
import com.keelim.comssa.ui.screen.ComssaHost

@Stable data class Category(val title: String, val onClick: () -> Unit)

@Composable
fun ComssaApp(
    windowSizeClass: WindowSizeClass,
    appState: AppState = rememberAppState(windowSizeClass = windowSizeClass),
) {
  var selectedItem by rememberSaveable { mutableIntStateOf(0) }
  val navItems =
      listOf(
          Category(
              title = "이달 보기",
              onClick = {
                // appState.navController.navigate("")
              }),
          Category(
              title = "연도 보기",
              onClick = {
                // appState.navController.navigate("")
              }),
          Category(title = "전체 보기", onClick = {}),
      )

  NavigationSuiteScaffold(
      navigationSuiteItems = {
        navItems.fastForEachIndexed { index, navItem ->
          item(
              icon = {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = navItem.title)
              },
              label = { Text(text = navItem.title) },
              selected = selectedItem == index,
              onClick = {
                selectedItem = index
                navItem.onClick.invoke()
              })
        }
      }) {
        val snackbarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()
        val bottomSheetState = rememberModalBottomSheetState()
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { padding ->
          Row(
              Modifier.fillMaxSize()
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
}
