@file:OptIn(
    ExperimentalFoundationApi::class,
)

package com.keelim.cnubus.ui.screen.main

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.cnubus.ui.screen.root.RootRoute
import com.keelim.cnubus.ui.screen.root.RootViewModel
import com.keelim.cnubus.ui.screen.setting.ScreenAction.AppSetting
import com.keelim.cnubus.ui.screen.setting.ScreenAction.Map
import com.keelim.cnubus.ui.screen.setting.ScreenAction.Update
import com.keelim.cnubus.ui.screen.setting.SettingScreen
import com.keelim.common.extensions.toast
import com.keelim.composeutil.resource.space8
import kotlinx.coroutines.launch

private val appPermissions: List<String> = buildList {
    add(Manifest.permission.ACCESS_FINE_LOCATION)
    add(Manifest.permission.ACCESS_COARSE_LOCATION)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun MainRoute(
    onNavigateMap: () -> Unit,
    viewModel: RootViewModel = hiltViewModel(),
) = trace("MainRoute") {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val responsePermissions = permissions.entries.filter { appPermissions.contains(it.key) }
        if (responsePermissions.filter { it.value }.size == appPermissions.size) {
            context.toast("권한이 확인되었습니다.")
        }
    }
    LaunchedEffect(launcher) {
        launcher.launch(appPermissions.toTypedArray())
    }

    MainScreen(
        onNavigateMap = onNavigateMap,
        onSetMode = viewModel::setMode,
    )
}

@Stable
data class TabItem(
    val title: String,
    val mode: String,
)

private val tabItems =
    listOf(
        TabItem(title = "A노선", mode = "a"),
        TabItem(title = "B노선", mode = "b"),
        TabItem(title = "C노선", mode = "c"),
        TabItem(title = "야간\n노선", mode = "d"),
        TabItem(title = "설정", mode = "e"),
    )

@Composable
fun MainScreen(
    onNavigateMap: () -> Unit,
    onSetMode: (String) -> Unit,
) = trace("MainScreen") {
    val pagerState = rememberPagerState { tabItems.size }
    Column {
        TabBarLayout(
            state = pagerState,
            onSetMode = onSetMode,
        )
        PagerContent(
            state = pagerState,
            onNavigateMap = onNavigateMap,
        )
    }
}

@Composable
fun TabBarLayout(
    state: PagerState,
    onSetMode: (String) -> Unit,
) = trace("TabBarLayout") {
    val coroutineScope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = state.currentPage,
        modifier = Modifier.fillMaxWidth(),
    ) {
        tabItems.fastForEachIndexed { index, tabItem ->
            Tab(
                selected = state.currentPage == index,
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToPage(index)
                        onSetMode(tabItem.mode)
                    }
                },
                text = {
                    Text(
                        text = tabItem.title,
                    )
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(
    state: PagerState,
    onNavigateMap: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = space8, vertical = space4),
) = trace("PagerContent") {
    val context = LocalContext.current
    HorizontalPager(
        state = state,
        userScrollEnabled = false,
        contentPadding = paddingValues,
    ) { index ->
        when (index) {
            0 -> {
                RootRoute(
                    onRootClick = {},
                )
            }
            1 -> {
                RootRoute(
                    onRootClick = {},
                )
            }
            2 -> {
                RootRoute(
                    onRootClick = {},
                )
            }
            3 -> {
                RootRoute(
                    onRootClick = {},
                )
            }
            4 -> {
                SettingScreen(
                    onScreenAction = { action ->
                        when (action) {
                            AppSetting -> {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://plus.cnu.ac.kr/html/kr/sub05/sub05_050403.html"),
                                    ),
                                )
                            }
                            Map -> onNavigateMap()
                            Update -> {
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.keelim.cnubus"),
                                    ),
                                )
                            }
                            else -> Unit
                        }
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onNavigateMap = {},
        onSetMode = {},
    )
}
