package com.keelim.mygrade.ui.screen.timer.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.model.TimerHistoryModel
import kotlinx.datetime.LocalDateTime

@Composable
fun TimerHistoryRoute(
    onSetTimer: (hours: Int, minutes: Int, seconds: Int) -> Unit = { _, _, _ -> },
    viewModel: TimerHistoryViewModel = hiltViewModel(),
) = trace("TimerHistoryModelRoute") {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimerHistoryScreen(
        uiState = uiState,
        onItemClick = { history ->
            onSetTimer(history.hours, history.minutes, history.seconds)
        },
        onDeleteItem = viewModel::deleteHistory,
        onDeleteAll = viewModel::deleteAll,
        onUpdateDescription = viewModel::updateDescription,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerHistoryScreen(
    uiState: TimerHistoryUiState,
    onItemClick: (TimerHistoryModel) -> Unit = {},
    onDeleteItem: (Int) -> Unit = {},
    onDeleteAll: () -> Unit = {},
    onUpdateDescription: (Int, String) -> Unit = { _, _ -> },
) = trace("TimerHistoryModelScreen") {
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var editingHistory by remember { mutableStateOf<TimerHistoryModel?>(null) }

    if (showDeleteAllDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllDialog = false },
            title = {
                Text(
                    text = "모두 삭제",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            text = {
                Text(
                    text = "모든 타이머 기록을 삭제하시겠습니까?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteAll()
                        showDeleteAllDialog = false
                    },
                ) {
                    Text(
                        text = "삭제",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllDialog = false }) {
                    Text(text = "취소")
                }
            },
        )
    }

    editingHistory?.let { history ->
        EditDescriptionDialog(
            currentDescription = history.description,
            onDismiss = { editingHistory = null },
            onConfirm = { newDescription ->
                onUpdateDescription(history.uid, newDescription)
                editingHistory = null
            },
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                    ),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Timer History",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "저장된 타이머 기록",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                if (uiState.histories.isNotEmpty()) {
                    FilledIconButton(
                        onClick = { showDeleteAllDialog = true },
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Delete All",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Loading()
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.histories.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                EmptyView(text = "아직 저장된 기록이 없습니다")
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.histories.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(
                        items = uiState.histories,
                        key = { it.uid },
                    ) { history ->
                        SwipeableHistoryItem(
                            history = history,
                            onClick = { onItemClick(history) },
                            onDelete = { onDeleteItem(history.uid) },
                            onEdit = { editingHistory = history },
                            modifier = Modifier.animateItem(),
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableHistoryItem(
    history: TimerHistoryModel,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isRemoved by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd ||
                dismissValue == SwipeToDismissBoxValue.EndToStart
            ) {
                isRemoved = true
                true
            } else {
                false
            }
        },
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier,
    ) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                val color by animateColorAsState(
                    targetValue = when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.Settled -> MaterialTheme.colorScheme.surfaceVariant
                        else -> MaterialTheme.colorScheme.errorContainer
                    },
                    label = "SwipeBackground",
                )
                val scale by animateFloatAsState(
                    targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                    label = "IconScale",
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color)
                        .padding(horizontal = 24.dp),
                    contentAlignment = if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.scale(scale),
                    )
                }
            },
            content = {
                HistoryItemCard(
                    history = history,
                    onClick = onClick,
                    onEdit = onEdit,
                )
            },
        )
    }
}

@Composable
private fun HistoryItemCard(
    history: TimerHistoryModel,
    onClick: () -> Unit,
    onEdit: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Timer Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(24.dp),
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Time and Date Info
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = history.formattedTime,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (history.description.isNotEmpty()) {
                    Text(
                        text = history.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = formatDate(history.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            // Edit Button
            FilledIconButton(
                onClick = onEdit,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                modifier = Modifier.size(40.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun EditDescriptionDialog(
    currentDescription: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var description by remember { mutableStateOf(currentDescription) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "설명 수정",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = {
                    Text(
                        text = "설명",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                placeholder = {
                    Text(
                        text = "예: 수학 공부, 독서 등",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(description) }) {
                Text(text = "저장")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "취소")
            }
        },
    )
}

private fun formatDate(dateString: String): String {
    return try {
        val dateTime = LocalDateTime.parse(dateString)
        "${dateTime.year}년 ${dateTime.monthNumber}월 ${dateTime.dayOfMonth}일 ${
            String.format(
                "%02d:%02d",
                dateTime.hour,
                dateTime.minute
            )
        }"
    } catch (e: Exception) {
        dateString
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTimerHistoryScreen() {
    TimerHistoryScreen(
        uiState = TimerHistoryUiState(
            histories = listOf(
                TimerHistoryModel(
                    uid = 1,
                    hours = 1,
                    minutes = 30,
                    seconds = 0,
                    description = "수학 공부",
                    date = "2024-01-15T14:30:00",
                    isCompleted = false
                ),
                TimerHistoryModel(
                    uid = 2,
                    hours = 0,
                    minutes = 25,
                    seconds = 0,
                    description = "",
                    date = "2024-01-15T10:00:00",
                    isCompleted = true
                ),
            ),
            isLoading = false,
        ),
    )
}
