package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.component.fab.FabButtonMain
import com.keelim.composeutil.component.fab.FabButtonState
import com.keelim.composeutil.component.fab.FabButtonSub
import com.keelim.composeutil.component.fab.MultiMainFab
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun EcocalRoute(viewModel: EcocalViewModel = hiltViewModel()) = trace("EcocalRoute") {
    val uiState by viewModel.items.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current,
    )
    EcocalScreen(
        uiState = uiState,
        updateFilter = viewModel::updateFilter,
        updateCountry = viewModel::updateCountry,
    )
}

@Composable
fun EcocalScreen(
    uiState: SealedUiState<Map<String, List<EcoCalModel>>>,
    updateFilter: (FabButtonItem) -> Unit,
    updateCountry: (String) -> Unit,
) = trace("EcocalScreen") {
    AnimatedContent(
        targetState = uiState,
        label = "",
    ) { targetState ->
        when (targetState) {
            is SealedUiState.Error -> EmptyView()
            SealedUiState.Loading -> Loading()
            is SealedUiState.Success -> {
                val state = rememberLazyListState()
                val coroutineScope = rememberCoroutineScope()
                Scaffold(
                    topBar = { HeaderItem() },
                    floatingActionButton = {
                        var fabState by remember { mutableStateOf<FabButtonState>(FabButtonState.Collapsed) }
                        val items by remember {
                            mutableStateOf(
                                listOf(
                                    High(),
                                    Medium(),
                                    Low(),
                                    Clear(),
                                ),
                            )
                        }
                        MultiMainFab(
                            fabState = fabState,
                            items = items,
                            fabIcon = FabButtonMain(),
                            fabOption = FabButtonSub(
                                backgroundTint = MaterialTheme.colorScheme.primary,
                                iconTint = MaterialTheme.colorScheme.onPrimary,
                            ),
                            onFabItemClicked = { item ->
                                Timber.d("item $item")
                                updateFilter(item)
                                fabState = fabState.toggleValue()
                                coroutineScope.launch {
                                    state.scrollToItem(0)
                                }
                            },
                            stateChanged = {
                                fabState = it
                            },
                        )
                    },
                ) { paddingValues ->
                    EcocalMainSection(
                        state = state,
                        entries = targetState.value,
                        modifier = Modifier
                            .padding(paddingValues),
                        onCountryClick = updateCountry,
                    )
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
