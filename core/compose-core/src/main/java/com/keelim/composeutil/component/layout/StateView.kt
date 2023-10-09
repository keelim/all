package com.keelim.composeutil.component.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Stable
sealed interface CommonScreenState {
    object Loading : CommonScreenState
    object Empty : CommonScreenState
    object Error : CommonScreenState
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

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(text = "로딩중...", modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun PreviewLoading() {
    Loading()
}