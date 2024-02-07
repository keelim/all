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
import com.keelim.composeutil.resource.space16

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

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator()
        Text(text = "로딩중...", modifier = Modifier.padding(space16))
    }
}

@Preview
@Composable
fun PreviewLoading() {
    Loading()
}
