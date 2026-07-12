@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.finance

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiEmptyState
import com.keelim.core.designsystem.component.KuiLoadingStatus
import com.keelim.core.designsystem.component.KuiLoadingVariant
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource as androidStringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.comssa.R
import com.keelim.model.finance.FinanceRssItem
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource
import com.keelim.web.navigateToWebModule

@Composable
fun FinanceRoute(
    viewModel: FinanceViewModel = hiltViewModel(),
) = trace("FinanceRoute") {
    val uiState by viewModel.items.collectAsStateWithLifecycle()
    FinanceScreen(
        uiState = uiState,
        updateFilter = viewModel::updateFilter,
        updateSource = viewModel::updateSource,
        refresh = viewModel::refresh,
    )
}

@Composable
fun FinanceScreen(
    uiState: SealedUiState<List<FinanceRssItem>>,
    updateFilter: (FabButtonItem) -> Unit,
    updateSource: (String) -> Unit,
    refresh: () -> Unit,
) = trace("FinanceScreen") {
    val context = LocalContext.current

    when (uiState) {
        is SealedUiState.Error -> {
            val errorTitle = androidStringResource(R.string.comssa_state_error_title)
            KuiEmptyState(
                title = errorTitle,
                description = androidStringResource(R.string.comssa_state_error_description),
                action = {
                    KuiButton(
                        text = androidStringResource(R.string.comssa_state_retry),
                        onClick = refresh,
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(KuiTheme.spacing.cardPadding),
            )
        }
        SealedUiState.Loading -> {
            val loadingLabel = androidStringResource(R.string.comssa_finance_loading)
            KuiLoadingStatus(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(KuiTheme.spacing.cardPadding),
                variant = KuiLoadingVariant.Panel,
                label = loadingLabel,
            )
        }
        is SealedUiState.Success -> {
            if (uiState.value.isEmpty()) {
                val emptyTitle = androidStringResource(R.string.comssa_finance_empty_title)
                KuiEmptyState(
                    title = emptyTitle,
                    description = androidStringResource(R.string.comssa_finance_empty_description),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(KuiTheme.spacing.cardPadding),
                )
            } else {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }

            val navigationIndex = remember { mutableIntStateOf(0) }

            KuiScaffold(
                floatingActionButton = {
                    if (navigationIndex.intValue == 0) {
                        AnimatedVisibility(
                            visible = showButton,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                        ) {
                            FinanceFloatingButton(
                                coroutineScope = coroutineScope,
                                listState = listState,
                                updateFilter = updateFilter,
                                refresh = refresh,
                            )
                        }
                    }
                },
                bottomBar = {
                    FinanceNavigationBar(
                        navigationIndex = navigationIndex,
                    )
                },
            ) { paddingValues ->
                if (navigationIndex.intValue == 0) {
                    FinanceMainSection(
                        state = listState,
                        items = uiState.value,
                        modifier = Modifier.padding(paddingValues),
                        onSourceClick = updateSource,
                        onItemClick = { item ->
                            context.navigateToWebModule(item.link.toUri())
                        },
                    )
                } else {
                    val pendingTitle = stringResource(Res.string.comssa_finance_settings_pending)
                    KuiEmptyState(
                        title = pendingTitle,
                        description = androidStringResource(
                            R.string.comssa_finance_settings_pending_description,
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(KuiTheme.spacing.cardPadding),
                    )
                }
            }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewFinanceScreen() {
    FinanceScreen(
        uiState = SealedUiState.success(
            listOf(
                FinanceRssItem(
                    title = "삼성전자 주가 상승",
                    description = "삼성전자 주가가 전일 대비 2% 상승했습니다.",
                    link = "https://example.com",
                    source = "한국경제",
                    category = "주식",
                ),
                FinanceRssItem(
                    title = "비트코인 가격 변동",
                    description = "비트코인 가격이 5만 달러를 돌파했습니다.",
                    link = "https://example.com",
                    source = "코인데스크",
                    category = "암호화폐",
                ),
            ),
        ),
        updateFilter = {},
        updateSource = {},
        refresh = {},
    )
}
