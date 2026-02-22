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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.keelim.cnubus.ui.screen.root.RootRoute
import com.keelim.cnubus.ui.screen.root.RootViewModel
import com.keelim.cnubus.ui.screen.setting.ScreenAction
import com.keelim.cnubus.ui.screen.setting.SettingScreen
import com.keelim.common.extensions.toast
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.core.resource.Res
import com.keelim.core.resource.cnubus_permission_granted
import com.keelim.core.resource.cnubus_tab_favorite
import com.keelim.core.resource.cnubus_tab_route_a
import com.keelim.core.resource.cnubus_tab_route_b
import com.keelim.core.resource.cnubus_tab_route_c
import com.keelim.core.resource.cnubus_tab_route_night
import com.keelim.core.resource.cnubus_tab_search
import com.keelim.core.resource.cnubus_tab_settings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

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
    val permissionGrantedMessage = stringResource(Res.string.cnubus_permission_granted)

    SimpleAcquirePermissions(
        appPermissions,
    ) {
        context.toast(permissionGrantedMessage)
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

@Composable
fun MainScreen(
    onNavigateMap: () -> Unit,
    onSetMode: (String) -> Unit,
    onNavigateAppSetting: () -> Unit,
) = trace("MainScreen") {
    val tabItems = listOf(
        TabItem(title = stringResource(Res.string.cnubus_tab_route_a), mode = "a"),
        TabItem(title = stringResource(Res.string.cnubus_tab_route_b), mode = "b"),
        TabItem(title = stringResource(Res.string.cnubus_tab_route_c), mode = "c"),
        TabItem(title = stringResource(Res.string.cnubus_tab_route_night), mode = "d"),
        TabItem(title = stringResource(Res.string.cnubus_tab_favorite), mode = "f"),
        TabItem(title = stringResource(Res.string.cnubus_tab_search), mode = "s"),
        TabItem(title = stringResource(Res.string.cnubus_tab_settings), mode = "e"),
    )
    val pagerState = rememberPagerState { tabItems.size }
    Column {
        TabBarLayout(
            state = pagerState,
            onSetMode = onSetMode,
            tabItems = tabItems,
        )
        PagerContent(
            state = pagerState,
            onNavigateAppSetting = onNavigateAppSetting,
            onNavigateMap = onNavigateMap,
            tabItems = tabItems,
        )
    }
}

@Composable
fun TabBarLayout(
    state: PagerState,
    onSetMode: (String) -> Unit,
    tabItems: List<TabItem>,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
    tabItems: List<TabItem>,
    paddingValues: PaddingValues = PaddingValues(horizontal = space8, vertical = space4),
) = trace("PagerContent") {
    val context = LocalContext.current
    HorizontalPager(
        state = state,
        userScrollEnabled = false,
        contentPadding = paddingValues,
    ) { index ->
        when (index) {
            6 -> SettingScreen(
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
