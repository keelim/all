package com.keelim.data.source

import com.keelim.data.source.local.LocalTask
import com.keelim.data.source.network.NetworkTask
import kotlinx.datetime.Clock

data class Task(
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val id: String,
) {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}


// Convert a LocalTask to a Task
fun LocalTask.toExternal() = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
)

// Convenience function which converts a list of LocalTasks to a list of Tasks
fun List<LocalTask>.toExternal() = map(LocalTask::toExternal) // Equivalent to map { it.toExternal() }

fun Task.toLocal() = LocalTask(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    date = Clock.System.now().toString()
)

fun NetworkTask.toLocal() = LocalTask(
    id = id,
    title = title,
    description = shortDescription,
    isCompleted = (status == NetworkTask.TaskStatus.COMPLETE),
    date = Clock.System.now().toString()
)

fun List<NetworkTask>.toLocal() = map(NetworkTask::toLocal)

fun LocalTask.toNetwork() = NetworkTask(
    id = id,
    title = title,
    shortDescription = description,
    status = if (isCompleted) {
        NetworkTask.TaskStatus.COMPLETE
    } else {
        NetworkTask.TaskStatus.ACTIVE
    }
)

fun List<LocalTask>.toNetwork() = map(LocalTask::toNetwork)

