package com.keelim.setting.screen.lab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LabRoute(viewModel: LabViewModel = hiltViewModel()) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  LabScreen(
      uiState = uiState,
      onClick = viewModel::queuePrompt
  )
}

@Composable
fun LabScreen(
    uiState: LabUiState,
    onClick: (String) -> Unit,
) {
    val (prompt, setPrompt) =  remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(all = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row {
            TextField(
                value = prompt,
                label = { Text(
                    text = "prompt 를 입력해주세요"
                ) },
                placeholder = { Text(
                    text = "요약해드립니다."
                ) },
                onValueChange = setPrompt,
                modifier = Modifier
                    .weight(8f)
            )
            TextButton(
                onClick = {
                    if (prompt.isNotBlank()) {
                        onClick(prompt)
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .padding(all = 4.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "Queue!"
                )
            }
        }
        when (uiState) {
            LabUiState.Initial,
            LabUiState.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                CircularProgressIndicator()
            }

            is LabUiState.Success -> Row(modifier = Modifier.padding(all = 8.dp)) {
                Icon(
                    Icons.Outlined.Person,
                    contentDescription = "Person Icon"
                )
                Text(
                    text = uiState.outputText,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            is LabUiState.Error -> Text(
                text = uiState.errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(all = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLabScreen() {
  LabScreen(
      uiState =LabUiState.Loading, onClick = {}
  )
}
