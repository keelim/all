package com.keelim.cnubus.ui.screen.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.map.screen.map1.MapEvent

@Composable
fun RootRoute(onRootClick: (Int) -> Unit) {
    RootScreen(onRootClick = onRootClick)
}

@Composable
fun RootScreen(onRootClick: (Int) -> Unit, viewModel: RootViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    RootStateView(
        uiState = uiState,
        onRootClick = onRootClick
    )
}

@Composable
fun RootStateView(
    uiState: MapEvent,
    onRootClick: (Int) -> Unit
) {
    when (uiState) {
        MapEvent.UnInitialized,
        is MapEvent.Error -> EmptyView()

        MapEvent.Loading -> Loading()
        is MapEvent.MigrateSuccess -> {
            if (uiState.data.isEmpty()) {
                EmptyView()
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    itemsIndexed(uiState.data) { index, item ->
                        RootCard(
                            position = index,
                            rootTitle = item.name,
                            onRootClick = onRootClick
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewRootScreen() {
    RootScreen(onRootClick = {})
}

@Composable
internal fun RootCard(
    position: Int,
    rootTitle: String,
    onRootClick: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        onClick = { onRootClick(position) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = rootTitle, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Preview
@Composable
private fun PreviewRootCard() {
    RootCard(onRootClick = {}, position = 6612, rootTitle = "velit")
}
