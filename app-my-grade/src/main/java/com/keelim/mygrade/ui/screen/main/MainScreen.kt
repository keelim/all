package com.keelim.mygrade.ui.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
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

@Composable
fun MainRoute(
    onSubmitClick: (NormalProbability, Int) -> Unit,
    onFloatingButtonClick1: () -> Unit,
    onFloatingButtonClick2: () -> Unit,
    onFloatingButtonClick3: () -> Unit,
) {
    MainScreen(
        onSubmitClick = onSubmitClick,
        onFloatingButtonClick1 = onFloatingButtonClick1,
        onFloatingButtonClick2 = onFloatingButtonClick2,
        onFloatingButtonClick3 = onFloatingButtonClick3,
    )
}

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onSubmitClick: (NormalProbability, Int) -> Unit = { _, _ -> },
    onFloatingButtonClick1: () -> Unit = {},
    onFloatingButtonClick2: () -> Unit = {},
    onFloatingButtonClick3: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        Text(text = "MyGrade", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(10.dp))
        // 원점수
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {}

        val mainState by viewModel.mainScreenState.collectAsStateWithLifecycle()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val origin by viewModel.origin.collectAsStateWithLifecycle()
        val average by viewModel.average.collectAsStateWithLifecycle()
        val number by viewModel.number.collectAsStateWithLifecycle()
        val student by viewModel.student.collectAsStateWithLifecycle()
        if (state is MainState.Success) {
            SideEffect {
                onSubmitClick(
                    (state as MainState.Success).value,
                    (state as MainState.Success).student,
                )
                viewModel.moveToUnInitialized()
            }
        }

        // 과목 평균
        ScoreTextRow(
            text = "원점수",
            value = origin,
            onValueChange = viewModel::updateOrigin,
            isError = mainState.originError,
        )
        ScoreTextRow(
            text = "과목 평균",
            value = average,
            onValueChange = viewModel::updateAverage,
            isError = mainState.averageError,
        )
        ScoreTextRow(
            text = "표준편차",
            value = number,
            onValueChange = viewModel::updateNumber,
            isError = mainState.numberError,
        )
        ScoreTextRow(
            text = "학생 수",
            value = student,
            onValueChange = viewModel::updateStudent,
            isError = mainState.studentError,
        )
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { viewModel.submit() }) {
                Text(text = "Submit", style = MaterialTheme.typography.labelLarge)
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Column {
            Row {
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(onClick = { onFloatingButtonClick1() }) {
                    Icon(imageVector = Icons.Rounded.ThumbUp, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                FloatingActionButton(onClick = { onFloatingButtonClick3() }) {
                    Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                }
                Spacer(modifier = Modifier.weight(1f))
                FloatingActionButton(onClick = { onFloatingButtonClick2() }) {
                    Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewMainScreen() {
    MainScreen()
}

@Composable
internal fun ScoreTextRow(
    text: String,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.width(20.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            label = if (isError) {
                {
                    Text(
                        text = "형식을 다시 써주세요",
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            } else {
                null
            },
            placeholder = {
                Text(
                    text = "$text 입력해주세요.",
                    style = MaterialTheme.typography.labelLarge,
                )
            },
            leadingIcon = { Icon(imageVector = Icons.Rounded.Create, contentDescription = null) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    Spacer(modifier = Modifier.height(36.dp))
}

@Preview(showBackground = true)
@Composable
private fun PreviewScoreTextRow() {
    ScoreTextRow(text = "원점수", value = "", onValueChange = {}, isError = false)
}
