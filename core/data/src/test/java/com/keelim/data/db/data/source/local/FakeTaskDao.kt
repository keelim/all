package com.keelim.data.db.data.source.local

import com.keelim.data.db.dao.TaskDao
import com.keelim.data.source.local.LocalTask
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeTaskDao(initialTasks: List<LocalTask>) : TaskDao {

    private val _tasks = initialTasks.toMutableList()
    private val tasksStream = MutableStateFlow(_tasks.toList())

    override fun observeAll(): Flow<List<LocalTask>> = tasksStream

    override suspend fun upsert(task: LocalTask) {
        _tasks.removeIf { it.id == task.id }
        _tasks.add(task)
        tasksStream.emit(_tasks)
    }

    override suspend fun upsertAll(tasks: List<LocalTask>) {
        val newTaskIds = tasks.map { it.id }
        _tasks.removeIf { newTaskIds.contains(it.id) }
        _tasks.addAll(tasks)
    }

    override suspend fun updateCompleted(taskId: String, completed: Boolean) {
        _tasks.firstOrNull { it.id == taskId }?.let { it.isCompleted = completed }
        tasksStream.emit(_tasks)
    }

    override suspend fun deleteAll() {
        _tasks.clear()
        tasksStream.emit(_tasks)
    }
}
