package com.keelim.mygrade.ui.screen.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.composeutil.component.layout.EmptyView
import com.keelim.composeutil.resource.space4
import com.keelim.data.source.local.LocalTask

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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "MyGrade") },
                    actions = {
                        IconButton(onClick = onAddLocalTask) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                        }
                    },
                )
            },
            floatingActionButton = {
                Column(verticalArrangement = Arrangement.spacedBy(space4)) {
                    FloatingActionButton(onClick = onNavigateChart) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = null,
                        )
                    }
                    SmallFloatingActionButton(onClick = onClear) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = null,
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
