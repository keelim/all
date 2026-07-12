package com.keelim.nandadiagnosis.ui.screen.nutrient

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.outlined.Favorite
import com.keelim.core.designsystem.component.KuiButton
import androidx.compose.material3.ButtonDefaults
import com.keelim.core.designsystem.component.KuiCard
import com.keelim.core.designsystem.component.KuiEmptyState
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiLoadingStatus
import com.keelim.core.designsystem.component.KuiLoadingVariant
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.keelim.composeutil.component.appbar.NavigationBackArrowBar
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.core.resource.Res
import com.keelim.core.resource.common_action_retry
import com.keelim.core.resource.nanda_nutrient_image_description
import com.keelim.core.resource.nanda_nutrient_open_description
import com.keelim.core.resource.nanda_nutrient_screen_title
import com.keelim.core.resource.nanda_nutrient_timer_action
import com.keelim.core.resource.nanda_state_empty_description
import com.keelim.core.resource.nanda_state_empty_title
import com.keelim.core.resource.nanda_state_error_description
import com.keelim.core.resource.nanda_state_error_title
import com.keelim.core.resource.nanda_state_loading
import org.jetbrains.compose.resources.stringResource

@Composable
fun NutrientRoute(
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
    viewModel: NutrientViewModel = hiltViewModel(),
) = trace("NutrientRoute") {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    NutrientScreen(
        uiState = uiState,
        onNutrientClick = onNutrientClick,
        onNutrientTimerClick = onNutrientTimerClick,
        onRetry = viewModel::retry,
    )
}

@Composable
private fun NutrientScreen(
    uiState: NutrientState,
    onNutrientClick: (String, String) -> Unit,
    onNutrientTimerClick: () -> Unit,
    onRetry: () -> Unit = {},
) = trace("NutrientScreen") {
    KuiScaffold(
        floatingActionButton = {
            KuiFloatingActionButton(
                onClick = onNutrientTimerClick,
            ) {
                KuiIcon(
                    imageVector = Icons.Filled.Call,
                    contentDescription = stringResource(Res.string.nanda_nutrient_timer_action),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
        ) {
            NavigationBackArrowBar(title = stringResource(Res.string.nanda_nutrient_screen_title))
            NutrientStateView(
                uiState = uiState,
                onNutrientClick = onNutrientClick,
                onRetry = onRetry,
            )
        }
    }
}

@Composable
private fun NutrientStateView(
    uiState: NutrientState,
    onNutrientClick: (String, String) -> Unit,
    onRetry: () -> Unit,
) =
    trace("NutrientStateView") {
        when (uiState) {
            NutrientState.Error -> KuiEmptyState(
                title = stringResource(Res.string.nanda_state_error_title),
                description = stringResource(Res.string.nanda_state_error_description),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(space16),
                action = {
                    KuiButton(
                        text = stringResource(Res.string.common_action_retry),
                        onClick = onRetry,
                    )
                },
            )
            NutrientState.Empty -> KuiEmptyState(
                title = stringResource(Res.string.nanda_state_empty_title),
                description = stringResource(Res.string.nanda_state_empty_description),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(space16),
            )

            NutrientState.Loading -> KuiLoadingStatus(
                modifier = Modifier.padding(space16),
                variant = KuiLoadingVariant.Panel,
                label = stringResource(Res.string.nanda_state_loading),
            )
            is NutrientState.Success -> {
                LazyColumn {
                    items(uiState.items, key = { it.first }) { (title, uri) ->
                        NutrientCard(title = title, uri = uri, onNutrientClick = { onNutrientClick(title, uri) })
                        Spacer(modifier = Modifier.height(space4))
                    }
                }
            }
        }
    }

@Preview(showBackground = true)
@Composable
fun PreviewNutrientScreen() {
    NutrientScreen(
        uiState = NutrientState.Empty,
        onNutrientClick = { _, _ -> },
        onNutrientTimerClick = {},
    )
}

@Composable
private fun NutrientCard(title: String, uri: String, onNutrientClick: () -> Unit) = trace("NutrientCard") {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier =
            Modifier
                .widthIn(max = 400.dp)
                .clip(KuiTheme.shapes.large)
                .clickable { onNutrientClick() }
                .padding(space16),
        ) {
        KuiCard(padded = false, modifier = Modifier.fillMaxWidth()) {
                Box {
                    AsyncImage(
                        model =
                        "https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1024&q=80",
                        modifier =
                        Modifier
                            .clip(KuiTheme.shapes.medium)
                            .aspectRatio(16 / 9f)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                        contentDescription = stringResource(
                            Res.string.nanda_nutrient_image_description,
                            title,
                        ),
                    )
                    KuiButton(
                        onClick = { onNutrientClick() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(space16),
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor = KuiTheme.colorScheme.surface,
                            contentColor = KuiTheme.colorScheme.onSurface,
                        ),
                    ) {
                        KuiIcon(
                            Icons.Outlined.Favorite,
                            contentDescription = stringResource(
                                Res.string.nanda_nutrient_open_description,
                                title,
                            ),
                        )
                    }
                }
            }
            Spacer(Modifier.height(space12))
            Row(horizontalArrangement = Arrangement.spacedBy(space12)) {
                Column(verticalArrangement = Arrangement.spacedBy(space4)) {
                    KuiText(
                        text = title,
                        maxLines = 1,
                        style = KuiTheme.typography.titleMedium,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                    // Row(horizontalArrangement = Arrangement.spacedBy(space8)) {
                    //   KuiText("4.5")
                    //   KuiIcon(Icons.Rounded.Star, contentDescription = null, tint = Color(0xFFFF9800))
                    // }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNutrientCard() {
    NutrientCard(
        title = "ridiculus",
        uri = "habemus",
        onNutrientClick = {},
    )
}
