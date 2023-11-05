@file:OptIn(ExperimentalFoundationApi::class)

package com.keelim.mygrade.ui.screen.task

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
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
        is SealedUiState.Success -> {
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
                            contentDescription = null
                        )
                    }
                }
            ) { paddingValues ->
                LocalTaskList(
                    items = (state as SealedUiState.Success<List<LocalTask>>).value,
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
private fun PreviewTaskScreen() {
    TaskScreen()
}

@Composable
fun LocalTaskList(
    items: List<LocalTask>,
    onChange: (task: LocalTask) -> Unit,
    onDelete: (task: LocalTask) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) {
        EmptyView()
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            itemsIndexed(items = items) { i, task ->
                LocalTaskItem(
                    item = task,
                    onChange = onChange,
                    onDelete = onDelete,
                    modifier = Modifier
                        .animateItemPlacement(
                            animationSpec = tween(
                                durationMillis  = 600
                            )
                        )
                )
            }
        }
    }
}

@Composable
fun LocalTaskItem(
    item: LocalTask,
    onChange: (task: LocalTask) -> Unit,
    onDelete: (task: LocalTask) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier =
        modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth().pointerInput(Unit) {
            detectTapGestures(onLongPress = { onDelete(item) })
        },
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            if (item.isCompleted.not()) {
                TextField(
                    value = item.title,
                    onValueChange = { onChange(item.copy(title = it)) },
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onChange(item.copy(isCompleted = false)) },
                ) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = null)
                }
            } else {
                Text(
                    text = item.title,
                    textDecoration =
                    if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                )
                Checkbox(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    checked = item.isCompleted,
                    onCheckedChange = { onChange(item.copy(isCompleted = it)) },
                )
            }
        }
    }
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
