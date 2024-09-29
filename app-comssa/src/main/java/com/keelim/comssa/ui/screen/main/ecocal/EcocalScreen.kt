package com.keelim.comssa.ui.screen.main.ecocal

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.composeutil.component.fab.FabButtonMain
import com.keelim.composeutil.component.fab.FabButtonState
import com.keelim.composeutil.component.fab.FabButtonSub
import com.keelim.composeutil.component.fab.MultiMainFab
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.util.permission.SimpleAcquirePermissions
import kotlinx.coroutines.launch
import timber.log.Timber

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

    AnimatedContent(
        targetState = uiState,
        label = "",
    ) { targetState ->
        when (targetState) {
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
                        Column {
                            AnimatedVisibility(
                                visible = showButton,
                                enter = fadeIn(),
                                exit = fadeOut(),
                            ) {
                                TopScrollButton(
                                    onScrollToTop = {
                                        coroutineScope.launch {
                                            listState.animateScrollToItem(0)
                                        }
                                    },
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
                                        listState.scrollToItem(0)
                                    }
                                },
                                stateChanged = {
                                    fabState = it
                                },
                            )
                        }
                    },
                ) { paddingValues ->
                    EcocalMainSection(
                        state = listState,
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

@Composable
private fun TopScrollButton(
    onScrollToTop: () -> Unit,
) {
    FloatingActionButton(
        modifier = Modifier
            .padding(16.dp)
            .size(50.dp),
        onClick = onScrollToTop,
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            contentDescription = "scroll to top",
        )
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
