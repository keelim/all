@file:OptIn(ExperimentalMaterial3Api::class)

package com.keelim.mygrade.ui.screen.task

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.data.source.Task
import kotlinx.collections.immutable.PersistentList

@Composable
fun TaskRoute(onTaskClick: () -> Unit) {
  TaskScreen()
}

@Composable
private fun TaskScreen(
  viewModel: TaskViewModel = hiltViewModel(),
  onTaskClick: () -> Unit = {},
) {
  val uiState by viewModel.taskUiState.collectAsStateWithLifecycle()
  TaskStateSection(uiState = uiState, onTaskClick = onTaskClick)
}

@Composable
private fun TaskStateSection(
  uiState: TaskUiState,
  onTaskClick: () -> Unit,
) {
  when (uiState) {
    TaskUiState.Error,
    TaskUiState.Empty -> EmptyView()
    TaskUiState.Loading -> Loading()
    is TaskUiState.Success -> TaskSuccessSection(tasks = uiState.tasks, onTaskClick = onTaskClick)
  }
}

@Composable private fun TaskSuccessSection(
    tasks: PersistentList<Task>,
    onTaskClick: () -> Unit
) {

}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskScreen() {
  TaskScreen()
}
