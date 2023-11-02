package com.keelim.data.source.network

data class NetworkTask(
    val id: String,
    val title: String,
    val shortDescription: String,
    val priority: Int? = null,
    val status: TaskStatus = TaskStatus.ACTIVE,
) {
    enum class TaskStatus {
        ACTIVE,
        COMPLETE,
    }
}
