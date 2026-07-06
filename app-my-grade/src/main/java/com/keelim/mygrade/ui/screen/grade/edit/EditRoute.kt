package com.keelim.mygrade.ui.screen.grade.edit

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiExtendedFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiFilledTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.component.layout.Loading
import com.keelim.composeutil.resource.space12
import com.keelim.composeutil.resource.space2
import com.keelim.composeutil.resource.space8

@Composable
fun EditRoute(
    viewModel: EditViewModel = hiltViewModel(),
) = trace("EditRoute") {
    val editUiState by viewModel.data.collectAsStateWithLifecycle()

    EditScreen(
        editUiState = editUiState,
        onAddClick = viewModel::updateNote,
        onClearDialog = viewModel::clearDialogState,
    )
}

@Composable
fun EditScreen(
    editUiState: SealedUiState<EditUiState>,
    onAddClick: (String) -> Unit,
    onClearDialog: () -> Unit,
) = trace("EditScreen") {
    AnimatedContent(
        targetState = editUiState,
        label = "",
    ) { targetState ->
        when (targetState) {
            SealedUiState.Loading -> Loading()
            is SealedUiState.Error -> EmptyView()
            is SealedUiState.Success -> EditSuccessSection(
                editUiState = targetState,
                onAddClick = onAddClick,
                onClearDialog = onClearDialog,
            )
        }
    }
}

@Composable
fun EditSuccessSection(
    editUiState: SealedUiState.Success<EditUiState>,
    onAddClick: (String) -> Unit,
    onClearDialog: () -> Unit,
) = trace("EditSuccessSection") {
    val (editResult, description, dialogState) = editUiState.value
    if (dialogState == EditDialogState.Success) {
        val backPressedDispatcher = LocalOnBackPressedDispatcherOwner
            .current?.onBackPressedDispatcher
        EditDialog(
            title = "등록 완료",
            description = "$editResult 노트 등록이 완료되었습니다. (${description.firstOrNull()}...",
            onClick = {
                backPressedDispatcher?.onBackPressed()
            },
            onDismiss = onClearDialog,
        )
    }
    if (dialogState == EditDialogState.Failed) {
        EditDialog(
            title = "등록 실패",
            description = "$editResult 노트 등록이 실패되었습니다. (${description.first()}...",
            onClick = onClearDialog,
            onDismiss = onClearDialog,
        )
    }

    var inputTitle by rememberSaveable {
        mutableStateOf("")
    }

    KuiScaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = space8, horizontal = space12),
        floatingActionButton = {
            KuiExtendedFloatingActionButton(
                onClick = {
                    onAddClick(inputTitle)
                },
                text = {
                    KuiText(text = "+ Add", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                },
                icon = {
                    KuiIcon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "",
                    )
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(space8),
        ) {
            KuiText(
                text = editResult.subject,
                style = KuiTheme.typography.displaySmall,
            )
            KuiFilledTextField(
                value = inputTitle,
                onValueChange = { newValue ->
                    inputTitle = newValue
                },
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = KuiTheme.colorScheme.onSurface,
                ),
                singleLine = false,
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize(),
                placeholder = {
                    KuiText(
                        text = "메모를 입력해주세요.",
                        fontSize = 18.sp,
                        color = KuiTheme.colorScheme.onSurface,
                    )
                },
            )
        }
    }
}

@Composable
private fun EditDialog(
    title: String,
    description: String,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) = trace("EditDialog") {
    var isDialogOpen by rememberSaveable {
        mutableStateOf(true)
    }
    if (isDialogOpen) {
        KuiAlertDialog(
            onDismissRequest = {
                isDialogOpen = false
                onDismiss()
            },
            title = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(space2),
                ) {
                    KuiText(text = title)
                }
            },
            text = {
                KuiText(text = description)
            },
            confirmButton = {
                KuiButton(
                    onClick = {
                        onClick()
                        isDialogOpen = false
                    },
                ) {
                    KuiText(
                        text = "확인",
                    )
                }
            },
            dismissButton = {
                KuiButton(
                    onClick = {
                        isDialogOpen = false
                        onDismiss()
                    },
                ) {
                    KuiText(
                        text = "취소",
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun PreviewEditScreen() {
    EditScreen(
        editUiState = SealedUiState.success(
            value = EditUiState(
                editResult = EditResult(
                    subject = "subject",
                ),
                descriptions = "descriptions",
            ),
        ),
        onAddClick = {},
        onClearDialog = {},
    )
}
