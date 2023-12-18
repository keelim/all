@file:OptIn(
    ExperimentalFoundationApi::class
)

package com.keelim.cnubus.ui.screen.main

import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.hilt.navigation.compose.hiltViewModel
import com.keelim.cnubus.ui.screen.root.RootRoute
import com.keelim.cnubus.ui.screen.root.RootViewModel
import com.keelim.cnubus.ui.screen.setting.ScreenAction.AppSetting
import com.keelim.cnubus.ui.screen.setting.ScreenAction.Map
import com.keelim.cnubus.ui.screen.setting.ScreenAction.Update
import com.keelim.cnubus.ui.screen.setting.SettingScreen
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
    onNavigateMap:() -> Unit,
    viewModel: RootViewModel = hiltViewModel(),
    ) {
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
) {
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
) {
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
                        text = tabItem.title
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
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
) {
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
                        when(action) {
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
                                        Uri.parse("https://play.google.com/store/apps/details?id=com.keelim.cnubus")
                                    )
                                )
                            }
                            else -> Unit
                        }
                    }
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
