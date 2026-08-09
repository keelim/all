package com.keelim.setting.screen.lab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import com.keelim.core.designsystem.component.KuiCircularProgressIndicator
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun LabRoute(viewModel: LabViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LabScreen(
        uiState = uiState,
        onClick = viewModel::queuePrompt,
    )
}

@Composable
fun LabScreen(
    uiState: LabUiState,
    onClick: (String) -> Unit,
) {
    val (prompt, setPrompt) = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .padding(all = space8)
            .verticalScroll(rememberScrollState()),
    ) {
        Row {
            KuiFilledTextField(
                value = prompt,
                label = {
                    KuiText(
                        text = stringResource(Res.string.settings_lab_prompt_label),
                    )
                },
                placeholder = {
                    KuiText(
                        text = stringResource(Res.string.settings_lab_prompt_placeholder),
                    )
                },
                onValueChange = setPrompt,
                modifier = Modifier
                    .weight(8f),
            )
            KuiTextButton(
                onClick = {
                    if (prompt.isNotBlank()) {
                        onClick(prompt)
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .padding(all = space4)
                    .align(Alignment.CenterVertically),
            ) {
                KuiText(
                    text = stringResource(Res.string.settings_lab_queue),
                )
            }
        }
        when (uiState) {
            LabUiState.Initial,
            LabUiState.Loading,
            -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(all = space8)
                    .align(Alignment.CenterHorizontally),
            ) {
                KuiCircularProgressIndicator()
            }

            is LabUiState.Success -> Row(modifier = Modifier.padding(all = space8)) {
                KuiIcon(
                    Icons.Outlined.Person,
                    contentDescription = null,
                )
                KuiText(
                    text = uiState.outputText,
                    modifier = Modifier.padding(horizontal = space8),
                )
            }

            is LabUiState.Error -> KuiText(
                text = uiState.errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(all = space8),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewLabScreen() {
    LabScreen(
        uiState = LabUiState.Loading,
        onClick = {},
    )
}
