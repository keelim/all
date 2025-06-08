package com.keelim.composeutil.component.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Stable
sealed interface CommonScreenState {
    data object Loading : CommonScreenState
    data object Empty : CommonScreenState
    data object Error : CommonScreenState
    data class Success(val items: Any) : CommonScreenState
}

@Composable
fun CommonScreenStateView(
    uiState: CommonScreenState,
    successContent: @Composable (Any) -> Unit,
    loadingContent: @Composable () -> Unit = { Loading() },
    errorContent: @Composable () -> Unit = { EmptyView() },
) {
    when (uiState) {
        CommonScreenState.Loading -> loadingContent()
        CommonScreenState.Empty, CommonScreenState.Error -> errorContent()
        is CommonScreenState.Success -> successContent(uiState.items)
    }
}

