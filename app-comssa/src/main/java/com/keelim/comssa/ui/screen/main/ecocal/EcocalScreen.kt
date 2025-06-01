@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package com.keelim.comssa.ui.screen.main.ecocal

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions

@Composable
fun EcocalRoute(viewModel: EcocalViewModel = hiltViewModel()) = trace("EcocalRoute") {
    val uiState by viewModel.items.collectAsStateWithLifecycle()
    EcocalScreen(
        uiState = uiState,
        updateFilter = viewModel::updateFilter,
        updateCountry = viewModel::updateCountry,
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
) = trace("EcocalScreen") {
    SimpleAcquirePermissions(
        permissions = appPermissions,
    ) { }

    when (uiState) {
        is SealedUiState.Error -> EmptyView()
        SealedUiState.Loading -> Loading()
        is SealedUiState.Success -> {
            val listState = rememberLazyListState()
            val coroutineScope = rememberCoroutineScope()

            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0
                }
            }

            val navigationIndex = mutableIntStateOf(0)

            Scaffold(
                floatingActionButton = {
                    if (navigationIndex.value == 0) {
                        EcocalFloatingButton(
                            showButton = showButton,
                            coroutineScope = coroutineScope,
                            listState = listState,
                            updateFilter = updateFilter
                        )

                    }
                },

                bottomBar = {
                    EcocalNavigationBar(
                        navigationIndex = navigationIndex
                    )
                }
            ) { paddingValues ->
                if (navigationIndex.value == 0) {
                    EcocalMainSection(
                        state = listState,
                        entries = uiState.value,
                        modifier = Modifier
                            .padding(paddingValues),
                        onCountryClick = updateCountry,
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    ) {
                        Loading()
                        Text(
                            text = "현재 준비 중입니다. "
                        )
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
