package com.keelim.mygrade.ui.screen.task.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.component.layout.Loading

@Composable
fun TaskAddRoute(onAddFinish: () -> Unit) {
  TaskAddScreen(onAddFinish = onAddFinish)
}

@Composable
fun TaskAddScreen(onAddFinish: () -> Unit, viewModel: TaskAddViewModel = hiltViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  TaskStateSection(uiState = uiState, onAddFinish = onAddFinish, onAddClick = viewModel::addTask)
}

@Composable
private fun TaskStateSection(
  uiState: TaskAddState,
  onAddFinish: () -> Unit,
  onAddClick: (String, String) -> Unit
) {
  if (uiState == TaskAddState.Loading) {
    Loading()
  }
  if (uiState == TaskAddState.Success) {
    LaunchedEffect(key1 = uiState) { onAddFinish() }
  }
  TaskAddSection(
      onAddClick = onAddClick
  )
}

@Composable
private fun TaskAddSection(
    onAddClick: (String, String) -> Unit
) {

}

@Preview
@Composable
private fun PreviewTaskAddSection() {
  TaskAddSection(onAddClick = { _, _ -> })
}
