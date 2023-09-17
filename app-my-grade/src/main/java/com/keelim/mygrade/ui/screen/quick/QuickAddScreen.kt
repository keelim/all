@file:OptIn(SavedStateHandleSaveableApi::class)

package com.keelim.mygrade.ui.screen.quick

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import com.keelim.mygrade.ui.screen.main.NormalProbability

@Composable
fun QuickAddRoute(
    onNavigate: (String, NormalProbability, Int) -> Unit,
) {
    QuickAddScreen(
        onNavigate = onNavigate,
    )
}

@Composable
private fun QuickAddScreen(
    viewModel: QuickAddViewModel = hiltViewModel(),
    onNavigate: (String, NormalProbability, Int) -> Unit = { _, _, _ -> },
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            val uiState by viewModel.quickAddUiState.collectAsStateWithLifecycle()
            SideEffect {
                if (uiState.isValid()) {
                    onNavigate(uiState.subject,uiState.normalProbability, uiState.student)
                    viewModel.clear()
                }
            }
            TextField(
                value = viewModel.message,
                onValueChange = viewModel::update,
                isError = uiState.isError,
                label = if (uiState.isError) {
                    {
                        Text(
                            text = uiState.errorMessage,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                } else {
                    null
                },
                placeholder = {
                    Text(
                        text = "과목명, 원점수, 과목 평균, 표준편차, 학생수를 순서대로 입력하세요.",
                        style = MaterialTheme.typography.labelLarge,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = viewModel::submit) { Text(text = "확인  하기!") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewQuickAddScreen() {
    QuickAddScreen()
}
