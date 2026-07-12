package com.keelim.mygrade.ui.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import com.keelim.core.designsystem.component.KuiFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import com.keelim.core.designsystem.component.KuiIcon
import com.keelim.core.designsystem.component.KuiIconButton
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSmallFloatingActionButton
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.component.KuiTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space4
import com.keelim.model.LocalTask
import com.keelim.mygrade.R

sealed interface TaskElement {
    data class Header(
        val text: String,
    ) : TaskElement

    data class Item(
        val localTask: LocalTask,
        val role: Role,
    ) : TaskElement
    enum class Role {
        TOP, BOTTOM, MIDDLE, SINGLE
    }
}

internal val TaskElement.stableKey: String
    get() = when (this) {
        is TaskElement.Header -> "header:$text"
        is TaskElement.Item -> "task:${localTask.id}"
    }

data class TaskListSection(
    val header: String = "",
    private val tasks: List<LocalTask>,
) {
    val tasksRoles = tasks.associateWith { task ->
        when {
            tasks.size == 1 -> TaskElement.Role.SINGLE
            tasks.indexOf(task) == 0 -> TaskElement.Role.TOP
            tasks.indexOf(task) == tasks.lastIndex -> TaskElement.Role.BOTTOM
            else -> TaskElement.Role.MIDDLE
        }
    }
}

fun List<LocalTask>.toTaskListSections(): List<TaskListSection> {
    return partition { it.isCompleted }
        .let { (checked, unchecked) ->
            buildList {
                if (unchecked.isNotEmpty()) {
                    add(TaskListSection("Todo", unchecked))
                }
                if (checked.isNotEmpty()) {
                    add(TaskListSection("완료", checked))
                }
            }
        }
}

fun List<TaskListSection>.toTaskElement() = map { section ->
    buildList {
        add(TaskElement.Header(section.header))
        section.tasksRoles.forEach { (task, role) ->
            add(TaskElement.Item(task, role))
        }
    }
}.flatten()

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TaskSuccessSection(
    state: SealedUiState.Success<List<TaskElement>>,
    onAddLocalTask: () -> Unit,
    onNavigateChart: () -> Unit,
    onClear: () -> Unit,
    onEditTask: (LocalTask) -> Unit,
    onDeleteTask: (LocalTask) -> Unit,
) {
    if (state.value.isEmpty()) {
        EmptyView()
    } else {
        val (showDialog, setShowDialog) = rememberSaveable { mutableStateOf(false) }
        var deleteTask by rememberSaveable { mutableStateOf<LocalTask?>(null) }
        KuiScaffold(
            topBar = {
                KuiTopAppBar(
                    title = { KuiText(text = "MyGrade") },
                    actions = {
                        KuiIconButton(onClick = onAddLocalTask) {
                            KuiIcon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = stringResource(R.string.task_add_action),
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                Column(verticalArrangement = Arrangement.spacedBy(space4)) {
                    KuiFloatingActionButton(onClick = onNavigateChart) {
                        KuiIcon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = stringResource(R.string.task_chart_action),
                        )
                    }
                    KuiSmallFloatingActionButton(onClick = onClear) {
                        KuiIcon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.task_clear_action),
                        )
                    }
                }
            },
        ) { paddingValues ->
            LocalTaskList(
                state = state,
                onChange = onEditTask,
                onDelete = {
                    deleteTask = it
                    setShowDialog(true)
                },
                modifier = Modifier.padding(paddingValues),
            )
            if (showDialog) {
                DeleteDialog(
                    setShowDialog = setShowDialog,
                    onConfirm = { deleteTask?.also(onDeleteTask) },
                )
            }
        }
    }
}
