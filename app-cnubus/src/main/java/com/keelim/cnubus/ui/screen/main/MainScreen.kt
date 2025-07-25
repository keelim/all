package com.keelim.cnubus.ui.screen.main

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.trace
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.cnubus.ui.screen.root.RootRoute
import com.keelim.cnubus.ui.screen.root.RootViewModel
import com.keelim.cnubus.ui.screen.setting.ScreenAction
import com.keelim.cnubus.ui.screen.setting.SettingScreen
import com.keelim.common.extensions.toast
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
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
    onNavigateAppSetting: () -> Unit,
    viewModel: RootViewModel = hiltViewModel(),
) = trace("MainRoute") {
    val context = LocalContext.current

    SimpleAcquirePermissions(
        appPermissions,
    ) {
        context.toast("권한이 확인되었습니다.")
    }

    MainScreen(
        onNavigateMap = onNavigateMap,
        onSetMode = viewModel::setMode,
        onNavigateAppSetting = onNavigateAppSetting,
    )
}

@Stable
data class TabItem(
    val title: String,
    val mode: String,
)

private val tabItems =
    listOf(
        TabItem(title = "A 노선", mode = "a"),
        TabItem(title = "B 노선", mode = "b"),
        TabItem(title = "C 노선", mode = "c"),
        TabItem(title = "야간 노선", mode = "d"),
        TabItem(title = "설정", mode = "e"),
    )

@Composable
fun MainScreen(
    onNavigateMap: () -> Unit,
    onSetMode: (String) -> Unit,
    onNavigateAppSetting: () -> Unit,
) = trace("MainScreen") {
    val pagerState = rememberPagerState { tabItems.size }
    Column {
        TabBarLayout(
            state = pagerState,
            onSetMode = onSetMode,
        )
        PagerContent(
            state = pagerState,
            onNavigateAppSetting = onNavigateAppSetting,
            onNavigateMap = onNavigateMap,
        )
    }
}

@Composable
fun TabBarLayout(
    state: PagerState,
    onSetMode: (String) -> Unit,
    modifier: Modifier = Modifier,
) = trace("TabBarLayout") {
    val coroutineScope = rememberCoroutineScope()
    PrimaryScrollableTabRow(
        modifier = modifier,
        selectedTabIndex = state.currentPage,
    ) {
        tabItems.fastForEachIndexed { index, tabItem ->
            val selected = state.currentPage == index
            Tab(
                selected = selected,
                onClick = {
                    coroutineScope.launch {
                        state.animateScrollToPage(index)
                        onSetMode(tabItem.mode)
                    }
                },
                text = {
                    Text(
                        text = tabItem.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        ),
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
    onNavigateAppSetting: () -> Unit,
    paddingValues: PaddingValues = PaddingValues(horizontal = space8, vertical = space4),
) = trace("PagerContent") {
    val context = LocalContext.current
    HorizontalPager(
        state = state,
        userScrollEnabled = false,
        contentPadding = paddingValues,
    ) { index ->
        when (index) {
            4 -> SettingScreen(
                onScreenAction = { action ->
                    when (action) {
                        ScreenAction.Homepage -> {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    "https://plus.cnu.ac.kr/html/kr/sub05/sub05_050403.html".toUri(),
                                ),
                            )
                        }

                        ScreenAction.Map -> onNavigateMap()
                        ScreenAction.AppSetting -> onNavigateAppSetting()
                    }
                },
            )

            else -> RootRoute(
                onRootClick = {},
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen(
        onNavigateMap = {},
        onNavigateAppSetting = {},
        onSetMode = {},
    )
}
