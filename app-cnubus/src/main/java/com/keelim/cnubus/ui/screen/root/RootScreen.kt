package com.keelim.cnubus.ui.screen.root

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.data.model.Location

sealed class MapEvent {
    data object UnInitialized : MapEvent()
    data object Loading : MapEvent()
    data class MigrateSuccess(val data: List<Location>) : MapEvent()
    data class Error(val message: String = "에러가 발생하였습니다.") : MapEvent()
}

@Composable
fun RootRoute(onRootClick: (Int) -> Unit) {
    RootScreen(onRootClick = onRootClick)
}

@Composable
fun RootScreen(onRootClick: (Int) -> Unit, viewModel: RootViewModel = hiltViewModel()) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    RootStateView(
        uiState = uiState,
        onRootClick = onRootClick,
    )
}

@Composable
fun RootStateView(
    uiState: MapEvent,
    onRootClick: (Int) -> Unit,
) {
    when (uiState) {
        MapEvent.UnInitialized,
        is MapEvent.Error,
        -> EmptyView()

        MapEvent.Loading -> Loading()
        is MapEvent.MigrateSuccess -> {
            if (uiState.data.isEmpty()) {
                EmptyView()
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    itemsIndexed(uiState.data) { index, item ->
                        RootCard(
                            position = index,
                            rootTitle = item.name,
                            onRootClick = onRootClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRootScreen() {
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
        onClick = { onRootClick(position) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = rootTitle, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Preview
@Composable
fun PreviewRootCard() {
    RootCard(onRootClick = {}, position = 6612, rootTitle = "velit")
}

@Composable
fun RootTopBar(
    title: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRootTopBar() {
    RootTopBar(
        title = "assueverit",
    )
}

private val roots = listOf(
    "A노선",
    "B노선",
    "C노선",
    "야간노선",
    "설정",
)

@Preview
@Composable
fun ExposedDropdownMenuSample() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(roots[0]) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = {},
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            roots.fastForEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption) },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
