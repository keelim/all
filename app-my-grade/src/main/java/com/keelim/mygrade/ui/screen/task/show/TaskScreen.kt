@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.mygrade.ui.screen.task.show

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.data.source.local.LocalTask
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun TaskRoute(
    onTaskClick: () -> Unit,
    onNavigateTaskClick: () -> Unit,
) {
    TaskScreen(
        onTaskClick = onTaskClick,
        onNavigateTaskClick = onNavigateTaskClick,
    )
}

@Composable
private fun TaskScreen(
    viewModel: TaskViewModel = hiltViewModel(),
    onTaskClick: () -> Unit = {},
    onNavigateTaskClick: () -> Unit = {},
) {
    val uiState by viewModel.taskUiState.collectAsStateWithLifecycle()
    TaskStateSection(uiState = uiState, onTaskClick = onTaskClick, onNavigateTaskClick = onNavigateTaskClick)
}

@Composable
private fun TaskStateSection(
    uiState: TaskUiState,
    onTaskClick: () -> Unit,
    onNavigateTaskClick: () -> Unit,
) {
    var fabHeight by remember { mutableIntStateOf(0) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
                shape = CircleShape,
                onClick = { onNavigateTaskClick() },
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        when (uiState) {
            TaskUiState.Error,
            TaskUiState.Empty,
            -> EmptyView()
            TaskUiState.Loading -> Loading()
            is TaskUiState.Success -> {
                val modifier = Modifier.padding(paddingValues)
                TaskSuccessSection(tasks = uiState.tasks, modifier = modifier, onTaskClick = onTaskClick)
            }
        }
    }
}

@Composable
private fun TaskSuccessSection(
    tasks: PersistentList<LocalTask>,
    onTaskClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        items(tasks) { task ->
            TaskRow(
                task = task,
                onTaskClick = onTaskClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskSuccessSection() {
    TaskSuccessSection(tasks = persistentListOf(), onTaskClick = {})
}

@Composable
private fun TaskRow(
    task: LocalTask,
    onTaskClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = task.title, style = MaterialTheme.typography.titleLarge)
        Checkbox(checked = task.isCompleted, onCheckedChange = {
        })
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskRow() {
    Column {
        TaskRow(
            task = LocalTask(
                id = "pertinax",
                title = "decore",
                description = "persequeris",
                isCompleted = false,
                date = "epicurei",
            ),
            onTaskClick = {},
        )
        TaskRow(
            task = LocalTask(
                id = "pertinax",
                title = "decore",
                description = "persequeris",
                isCompleted = true,
                date = "epicurei",
            ),
            onTaskClick = {},
        )
    }
}
