package com.keelim.nandadiagnosis.ui.screen.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import com.keelim.core.designsystem.component.KuiAlertDialog
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCard
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.designsystem.component.KuiOutlinedTextField
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keelim.shared.data.database.model.ExerciseEntity
import com.keelim.core.resource.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExerciseRoute(
    viewModel: ExerciseViewModel = hiltViewModel(),
) {
    val exercises by viewModel.todayExercises.collectAsStateWithLifecycle()

    ExerciseScreen(
        exercises = exercises,
        onAddExercise = viewModel::addExercise,
        onDeleteExercise = viewModel::deleteExercise,
    )
}

@Composable
fun ExerciseScreen(
    exercises: List<ExerciseEntity>,
    onAddExercise: (String, String) -> Unit,
    onDeleteExercise: (Long) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    KuiScaffold(
        floatingActionButton = {
            KuiFloatingActionButton(onClick = { showDialog = true }) {
                KuiIcon(imageVector = Icons.Default.Add, contentDescription = stringResource(Res.string.nanda_exercise_add_description))
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
             if (exercises.isEmpty()) {
                KuiText(
                    text = stringResource(Res.string.nanda_exercise_empty),
                    style = KuiTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
             } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(exercises, key = { it.id }) { exercise ->
                        ExerciseItem(exercise = exercise, onDelete = { onDeleteExercise(exercise.id) })
                    }
                }
             }
        }
    }

    if (showDialog) {
        AddExerciseDialog(
            onDismiss = { showDialog = false },
            onConfirm = { title, duration ->
                onAddExercise(title, duration)
                showDialog = false
            }
        )
    }
}

@Composable
fun ExerciseItem(
    exercise: ExerciseEntity,
    onDelete: () -> Unit
) {
    KuiCard(padded = false,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                KuiText(
                    text = exercise.title,
                    style = KuiTheme.typography.titleMedium,
                    color = KuiTheme.colorScheme.onSurface
                )
                KuiText(
                    text = "${exercise.duration}",
                    style = KuiTheme.typography.bodyMedium,
                    color = KuiTheme.colorScheme.onSurfaceVariant
                )
            }
            KuiIconButton(onClick = onDelete) {
                KuiIcon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(Res.string.common_action_delete),
                    tint = KuiTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }

    KuiAlertDialog(
        onDismissRequest = onDismiss,
        title = { KuiText(text = stringResource(Res.string.nanda_exercise_dialog_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                KuiOutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { KuiText(stringResource(Res.string.nanda_exercise_name_label)) },
                    singleLine = true
                )
                KuiOutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { KuiText(stringResource(Res.string.nanda_exercise_duration_label)) },
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            KuiButton(
                onClick = { onConfirm(title, duration) },
                enabled = title.isNotBlank() && duration.isNotBlank()
            ) {
                KuiText(stringResource(Res.string.common_action_add))
            }
        },
        dismissButton = {
            KuiTextButton(onClick = onDismiss) {
                KuiText(stringResource(Res.string.common_action_cancel))
            }
        }
    )
}
