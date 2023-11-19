package com.keelim.mygrade.ui.screen.task

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
