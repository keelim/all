@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.Manifest
import android.os.Build
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import com.keelim.comssa.R

@Composable
fun EcocalRoute(
    viewModel: EcocalViewModel = hiltViewModel(),
    onNavigateToFinancialCalculators: () -> Unit = {},
) = trace("EcocalRoute") {
    val uiState by viewModel.items.collectAsStateWithLifecycle()
    EcocalScreen(
        uiState = uiState,
        updateFilter = viewModel::updateFilter,
        updateCountry = viewModel::updateCountry,
        onRetry = viewModel::retry,
        onNavigateToFinancialCalculators = onNavigateToFinancialCalculators,
    )
}

private val appPermissions: List<String> = buildList {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        add(Manifest.permission.POST_NOTIFICATIONS)
    }
}

@Composable
fun EcocalScreen(
    uiState: SealedUiState<Map<String, List<EcoCalModel>>>,
    updateFilter: (FabButtonItem) -> Unit,
    updateCountry: (String) -> Unit,
    onRetry: () -> Unit = {},
    onNavigateToFinancialCalculators: () -> Unit = {},
) = trace("EcocalScreen") {
    SimpleAcquirePermissions(
        permissions = appPermissions,
    ) { }

    when (uiState) {
        is SealedUiState.Error -> {
            val errorTitle = stringResource(R.string.comssa_state_error_title)
            KuiEmptyState(
                title = errorTitle,
                description = stringResource(R.string.comssa_state_error_description),
                action = {
                    KuiButton(
                        text = stringResource(R.string.comssa_state_retry),
                        onClick = onRetry,
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(KuiTheme.spacing.cardPadding),
            )
        }
        SealedUiState.Loading -> {
            val loadingLabel = stringResource(R.string.comssa_ecocal_loading)
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
                val emptyTitle = stringResource(R.string.comssa_ecocal_empty_title)
                KuiEmptyState(
                    title = emptyTitle,
                    description = stringResource(R.string.comssa_ecocal_empty_description),
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
                        EcocalFloatingButton(
                            showButton = showButton,
                            coroutineScope = coroutineScope,
                            listState = listState,
                            updateFilter = updateFilter,
                        )
                    }
                },

                bottomBar = {
                    EcocalNavigationBar(
                        navigationIndex = navigationIndex,
                    )
                },
            ) { paddingValues ->
                when (navigationIndex.intValue) {
                    0 -> {
                        EcocalMainSection(
                            state = listState,
                            entries = uiState.value,
                            modifier = Modifier
                                .padding(paddingValues),
                            onCountryClick = updateCountry,
                        )
                    }
                    1 -> {
                        val pendingTitle = stringResource(R.string.comssa_ecocal_settings_pending)
                        KuiEmptyState(
                            title = pendingTitle,
                            description = stringResource(
                                R.string.comssa_ecocal_settings_pending_description,
                            ),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(KuiTheme.spacing.cardPadding),
                        )
                    }
                    2 -> {
                        onNavigateToFinancialCalculators()
                    }
                }
            }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEcocalScreen() {
    EcocalScreen(
        uiState = SealedUiState.success(
            mapOf(
                "a" to listOf(
                    EcoCalModel(
                        country = "Congo, Democratic Republic of the",
                        date = "ridiculus",
                        priority = EcocalPriority.LOW,
                        time = "penatibus",
                        title = "option",
                    ),
                ),
            ),

        ),
        updateFilter = {},
        updateCountry = {},
    )
}
