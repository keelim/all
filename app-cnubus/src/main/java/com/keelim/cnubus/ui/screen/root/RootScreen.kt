package com.keelim.cnubus.ui.screen.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.trace
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space8
import com.keelim.core.data.model.Location

@Stable
sealed interface MapEvent {
    data object UnInitialized : MapEvent
    data object Loading : MapEvent
    data class MigrateSuccess(val data: List<Location>) : MapEvent
    data class Error(val message: String = "에러가 발생하였습니다.") : MapEvent
}

@Composable
fun RootRoute(
    onRootClick: (Int) -> Unit,
    viewModel: RootViewModel = hiltViewModel(),
) = trace("RootRoute") {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    RootScreen(
        uiState = uiState,
        onRootClick = onRootClick,
    )
}

@Composable
fun RootScreen(
    uiState: MapEvent,
    onRootClick: (Int) -> Unit,
) = trace("RootScreen") {
    when (uiState) {
        MapEvent.UnInitialized,
        is MapEvent.Error,
        -> EmptyView()
        MapEvent.Loading -> Loading()
        is MapEvent.MigrateSuccess -> {
            if (uiState.data.isEmpty()) {
                EmptyView()
            } else {
                Spacer(modifier = Modifier.height(space8))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = space12),
                    verticalArrangement = Arrangement.spacedBy(space24),
                ) {
                    itemsIndexed(uiState.data) { index, item ->
                        TimelineItem(
                            position = index,
                            rootTitle = item.name,
                            onRootClick = onRootClick,
                            isLast = index == uiState.data.size - 1,
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
    RootScreen(
        uiState = MapEvent.UnInitialized,
        onRootClick = {},
    )
}

@Composable
internal fun TimelineItem(
    position: Int,
    rootTitle: String,
    onRootClick: (Int) -> Unit,
    isLast: Boolean,
) = trace("TimelineItem") {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(space12)
    ) {
        // 타임라인 연결선과 아이콘
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            // 타임라인 아이콘
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when {
                        rootTitle.contains("노선") -> Icons.Default.LocationOn
                        rootTitle.contains("설정") -> Icons.Default.Settings
                        else -> Icons.Default.LocationOn
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // 연결선 (마지막 아이템이 아닌 경우에만)
            if (!isLast) {
                Spacer(modifier = Modifier.height(space8))
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                )
            }
        }

        // 카드 내용
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            onClick = { onRootClick(position) },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = rootTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "클릭하여 상세 정보 확인",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimelineItem() {
    TimelineItem(
        onRootClick = {},
        position = 0,
        rootTitle = "A노선",
        isLast = false
    )
}

@Composable
private fun RootTopBar(
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(space8),
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
private fun PreviewRootTopBar() {
    RootTopBar(
        title = "assueverit",
    )
}

val roots = listOf(
    "A노선",
    "B노선",
    "C노선",
    "야간노선",
    "설정",
)

@Preview
@Composable
private fun ExposedDropdownMenuSample() {
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
