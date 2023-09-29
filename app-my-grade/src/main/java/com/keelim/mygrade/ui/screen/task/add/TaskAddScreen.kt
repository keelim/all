package com.keelim.mygrade.ui.screen.task.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.Loading

@Composable
fun TaskAddRoute(onAddFinish: () -> Unit) {
  TaskAddScreen(onAddFinish = onAddFinish)
}

@Composable
fun TaskAddScreen(onAddFinish: () -> Unit, viewModel: TaskAddViewModel = hiltViewModel()) {
  val screenState by viewModel.screenState.collectAsStateWithLifecycle()
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()

  TaskStateSection(
    screenState = screenState,
    uiState = uiState,
    onAddFinish = onAddFinish,
    onAddClick = viewModel::addTask,
    onUpdateTitle = viewModel::updateTitle,
    onUpdateDescription = viewModel::updateDescription,
  )
}

@Composable
private fun TaskStateSection(
  screenState: TaskAddScreenState,
  uiState: TaskAddUiState,
  onAddFinish: () -> Unit,
  onAddClick: () -> Unit,
  onUpdateTitle: (String) -> Unit,
  onUpdateDescription: (String) -> Unit,
) {
  if (screenState == TaskAddScreenState.Loading) {
    Loading()
  }
  if (screenState == TaskAddScreenState.Success) {
      LaunchedEffect(Unit) {
          onAddFinish()
      }
  }

  TaskAddSection(
    title = uiState.title,
    description = uiState.description,
    onAddClick = onAddClick,
    onUpdateTitle = onUpdateTitle,
    onUpdateDescription = onUpdateDescription,
  )
}

@Composable
private fun TaskAddSection(
  title: String,
  description: String,
  onAddClick: () -> Unit,
  onUpdateTitle: (String) -> Unit,
  onUpdateDescription: (String) -> Unit,
) {
  Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp)) {
    Text(text = "Todo", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(12.dp))
    TaskAddRow(taskTitle = "할 일", taskDescription = title, onTaskValueChanged = onUpdateTitle)
    Spacer(modifier = Modifier.height(24.dp))
    TaskAddRow(
      taskTitle = "설명",
      taskDescription = description,
      onTaskValueChanged = onUpdateDescription
    )
    Spacer(modifier = Modifier.height(12.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
      Button(
        onClick = onAddClick,
      ) {
        Text(text = "Add Task")
      }
    }
  }
}

@Composable
private fun TaskAddRow(
  taskTitle: String,
  taskDescription: String,
  onTaskValueChanged: (String) -> Unit
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Text(
      text = taskTitle,
      textAlign = TextAlign.Center,
      modifier = Modifier.weight(1f),
    )
    Spacer(modifier = Modifier.width(8.dp))
    TextField(value = taskDescription, onValueChange = onTaskValueChanged)
  }
}

@Preview(showBackground = true)
@Composable
private fun PreviewTaskAddSection() {
  TaskAddSection(
    onAddClick = {},
    onUpdateTitle = {},
    onUpdateDescription = {},
    title = "eius",
    description = "euismod"
  )
}
