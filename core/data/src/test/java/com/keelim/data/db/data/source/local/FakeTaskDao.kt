package com.keelim.data.db.data.source.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskDao(initialTasks: List<com.keelim.core.database.model.LocalTask>) :
    com.keelim.core.database.dao.TaskDao {

    private val _tasks = initialTasks.toMutableList()
    private val tasksStream = MutableStateFlow(_tasks.toList())

    override fun observeAll(): Flow<List<com.keelim.core.database.model.LocalTask>> = tasksStream

    override suspend fun upsert(task: com.keelim.core.database.model.LocalTask) {
        _tasks.removeIf { it.id == task.id }
        _tasks.add(task)
        tasksStream.emit(_tasks)
    }

    override suspend fun upsertAll(tasks: List<com.keelim.core.database.model.LocalTask>) {
        val newTaskIds = tasks.map { it.id }
        _tasks.removeIf { newTaskIds.contains(it.id) }
        _tasks.addAll(tasks)
    }

    override suspend fun updateCompleted(taskId: String, completed: Boolean) {
        _tasks.firstOrNull { it.id == taskId }?.let { it.isCompleted = completed }
        tasksStream.emit(_tasks)
    }

    override suspend fun delete(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        _tasks.clear()
        tasksStream.emit(_tasks)
    }
}
