@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.mygrade.ui.screen.task

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.data.source.local.LocalTask

@Composable
fun TaskRoute() {
    TaskScreen()
}

@Composable
fun TaskScreen(viewModel: TaskViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    when (state) {
        SealedUiState.Loading,
        is SealedUiState.Error,
        -> EmptyView()
        is SealedUiState.Success<List<TaskElement>> -> {
            val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }
            var deleteTask by rememberSaveable { mutableStateOf<LocalTask?>(null) }
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "MyGrade") },
                        actions = {
                            IconButton(onClick = viewModel::addLocalTask) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                            }
                        },
                    )
                },
                floatingActionButton = {
                    SmallFloatingActionButton(onClick = viewModel::clear) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
                        )
                    }
                },
            ) { paddingValues ->
                LocalTaskList(
                    state = state,
                    onChange = viewModel::editTask,
                    onDelete = {
                        deleteTask = it
                        setShowDialog(true)
                    },
                    modifier = Modifier.padding(paddingValues),
                )
                if (showDialog) {
                    DeleteDialog(
                        setShowDialog = setShowDialog,
                        onConfirm = { deleteTask?.also(viewModel::deleteTask) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTaskScreen() {
    TaskScreen()
}

@Composable
fun LocalTaskList(
    state: SealedUiState<List<TaskElement>>,
    onChange: (task: LocalTask) -> Unit,
    onDelete: (task: LocalTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = state.getOrDefault(emptyList())
    if (items.isEmpty()) {
        EmptyView()
    } else {
        val selected by remember(items) {
            derivedStateOf {
                items.filterIsInstance(TaskElement.Header::class.java)
                    .takeIf { it.size >= 2 }?.let { 1 } ?: 0
            }
        }
        val spacedBy by animateDpAsState(Dp(selected * 2f), label = "")
        val innerCornerSize by animateDpAsState(Dp(selected * 4f), label = "")
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(spacedBy),
        ) {
            items(
                items = items,
            ) { task ->
                when (task) {
                    is TaskElement.Header -> LocalTaskHeader(
                        task = task,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = spring(),
                            ),
                    )
                    is TaskElement.Item -> LocalTaskItem(
                        item = task,
                        onChange = onChange,
                        onDelete = onDelete,
                        modifier = Modifier
                            .animateItemPlacement(
                                animationSpec = spring(),
                            ),
                        innerCornerSize = innerCornerSize,
                    )
                }
            }
        }
    }
}

@Composable
fun LocalTaskHeader(
    task: TaskElement.Header,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
    ) {
        Text(
            text = task.text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun LocalTaskItem(
    item: TaskElement.Item,
    onChange: (task: LocalTask) -> Unit,
    onDelete: (task: LocalTask) -> Unit,
    modifier: Modifier = Modifier,
    outerCornerSize: Dp = 20.dp,
    innerCornerSize: Dp = 0.dp,
) {
    val task = item.localTask
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onDelete(task) })
            },
        shape = item.role.toShape(outerCornerSize, innerCornerSize),
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (task.isEditing) {
                TextField(
                    value = task.title,
                    onValueChange = { onChange(task.copy(title = it)) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onChange(task.copy(isEditing = false)) },
                ) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                }
            } else {
                Text(
                    text = task.title,
                    textDecoration =
                    if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    checked = task.isCompleted,
                    onCheckedChange = { onChange(task.copy(isCompleted = it)) },
                )
            }
        }
    }
}

@Composable
private fun TaskElement.Role.toShape(outerCornerSize: Dp, innerCornerSize: Dp): Shape {
    val (outerCornerSizePx, innerCornerSizePx) = LocalDensity.current.run {
        outerCornerSize.toPx() to innerCornerSize.toPx()
    }

    val targetRect = remember(this, outerCornerSize, innerCornerSize) {
        when (this) {
            TaskElement.Role.TOP -> Rect(outerCornerSizePx, outerCornerSizePx, innerCornerSizePx, innerCornerSizePx)
            TaskElement.Role.BOTTOM -> Rect(innerCornerSizePx, innerCornerSizePx, outerCornerSizePx, outerCornerSizePx)
            TaskElement.Role.MIDDLE -> Rect(innerCornerSizePx, innerCornerSizePx, innerCornerSizePx, innerCornerSizePx)
            TaskElement.Role.SINGLE -> Rect(outerCornerSizePx, outerCornerSizePx, outerCornerSizePx, outerCornerSizePx)
        }
    }

    val animatedRect by animateRectAsState(targetRect, label = "")

    return RoundedCornerShape(
        animatedRect.left,
        animatedRect.top,
        animatedRect.right,
        animatedRect.bottom,
    )
}

@Composable
fun DeleteDialog(setShowDialog: (Boolean) -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = { setShowDialog(false) },
        title = { Text(text = "삭제") },
        text = { Text(text = "LocalTask 삭제") },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                    setShowDialog(false)
                },
            ) {
                Text(
                    text = "확인",
                )
            }
        },
        dismissButton = {
            Button(onClick = { setShowDialog(false) }) {
                Text(
                    text = "취소",
                )
            }
        },
    )
}
