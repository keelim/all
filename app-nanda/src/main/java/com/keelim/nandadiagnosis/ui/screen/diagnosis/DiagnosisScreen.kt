@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiEmptyState
import com.keelim.core.designsystem.component.KuiLoadingStatus
import com.keelim.core.designsystem.component.KuiLoadingVariant
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import com.keelim.core.designsystem.theme.KuiTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space16
import com.keelim.composeutil.resource.space4
import com.keelim.core.resource.Res
import com.keelim.core.resource.common_action_retry
import com.keelim.core.resource.nanda_state_empty_description
import com.keelim.core.resource.nanda_state_empty_title
import com.keelim.core.resource.nanda_state_error_description
import com.keelim.core.resource.nanda_state_error_title
import com.keelim.core.resource.nanda_state_loading
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiagnosisRoute(
    onDiagnosisClick: () -> Unit,
    viewModel: DiagnosisViewModel = hiltViewModel(),
) = trace("DiagnosisRoute") {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    DiagnosisScreen(
        screenState = screenState,
        onDiagnosisClick = onDiagnosisClick,
        query = query,
        onQueryChange = viewModel::search,
        onRetry = viewModel::retry,
    )
}

@Composable
fun DiagnosisScreen(
    screenState: DiagnosisScreenState,
    onDiagnosisClick: () -> Unit,
    query: String,
    onQueryChange: (String) -> Unit,
    onRetry: () -> Unit = {},
) = trace("DiagnosisScreen") {
    Column {
        KuiFilledTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
            placeholder = {
                KuiText(
                    text = "Search Diagnosis",
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                )
            },
            singleLine = true,
        )
        DiagnosisStateView(
            state = screenState,
            onDiagnosisClick = onDiagnosisClick,
            onRetry = onRetry,
        )
    }
}

@Composable
private fun DiagnosisStateView(
    state: DiagnosisScreenState,
    onDiagnosisClick: () -> Unit,
    onRetry: () -> Unit,
) = trace("DiagnosisStateView") {
    when (state) {
        DiagnosisScreenState.Error -> KuiEmptyState(
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
        DiagnosisScreenState.Empty -> KuiEmptyState(
            title = stringResource(Res.string.nanda_state_empty_title),
            description = stringResource(Res.string.nanda_state_empty_description),
            modifier = Modifier
                .fillMaxSize()
                .padding(space16),
        )

        DiagnosisScreenState.Loading -> KuiLoadingStatus(
            modifier = Modifier.padding(space16),
            variant = KuiLoadingVariant.Panel,
            label = stringResource(Res.string.nanda_state_loading),
        )
        is DiagnosisScreenState.Success -> {
            LazyColumn {
                items(
                    items = state.items,
                    key = { it.diagnosis }
                ) { item ->
                    DiagnosisItem(
                        title = item.diagnosis,
                        content = "",
                        label = "",
                        onDiagnosisClick = onDiagnosisClick,
                        modifier = Modifier.animateItem(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDiagnosisScreen() {
    DiagnosisScreen(
        screenState = DiagnosisScreenState.Empty,
        onDiagnosisClick = {},
        query = "",
        onQueryChange = {},
    )
}

@Composable
fun DiagnosisItem(
    title: String,
    content: String,
    label: String,
    onDiagnosisClick: () -> Unit,
    modifier: Modifier = Modifier,
) = trace("DiagnosisItem") {
    KuiSurface(
        onClick = { onDiagnosisClick() },
        shape = KuiTheme.shapes.large,
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space16),
            modifier = Modifier
                .fillMaxWidth()
                .padding(space16),
        ) {
            Column(Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    KuiText(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        style = KuiTheme.typography.bodyLarge,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                    KuiText(
                        text = label,
                        style = KuiTheme.typography.labelLarge,
                        color = KuiTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(space4))
                KuiText(
                    text = content,
                    style = KuiTheme.typography.bodyLarge,
                    color = KuiTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewDiagnosisItem() {
    DiagnosisItem(
        title = "Alice Bennett",
        content =
        "I miss you! It's been too long since we last caught up. Let's plan a coffee date soon!",
        label = "4d ago",
        onDiagnosisClick = {},
    )
}
