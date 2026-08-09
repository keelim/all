package com.keelim.core.database.mapper

import com.keelim.model.LocalTask

fun LocalTask.toLocalTaskEntity() = com.keelim.shared.data.database.model.LocalTask(
    id = id,
    description = description,
    date = date,
    isEditing = isEditing,
    title = title,
    isCompleted = isCompleted,
)

fun List<com.keelim.shared.data.database.model.LocalTask>.toLocalTask(): List<LocalTask> {
    return map { localTask ->
        LocalTask(
            id = localTask.id,
            title = localTask.title,
            description = localTask.description,
            isCompleted = localTask.isCompleted,
            date = localTask.date,
            isEditing = localTask.isEditing,
        )
    }
}
