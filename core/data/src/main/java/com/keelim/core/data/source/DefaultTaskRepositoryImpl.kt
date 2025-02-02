package com.keelim.core.data.source

import com.keelim.core.data.model.toLocal
import com.keelim.core.data.model.toNetwork
import com.keelim.core.database.dao.TaskDao
import com.keelim.core.database.model.LocalTask
import com.keelim.core.database.repository.DefaultTaskRepository
import com.keelim.core.network.Dispatcher
import com.keelim.core.network.KeelimDispatchers
import com.keelim.core.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class DefaultTaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskDao,
    private val networkDataSource: TaskNetworkDataSource,
    @Dispatcher(KeelimDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : DefaultTaskRepository {

    override fun observeAll(): Flow<List<LocalTask>> {
        return localDataSource.observeAll()
    }

    override suspend fun create() {
        val taskId = withContext(dispatcher) {
            createTaskId()
        }
        val task = LocalTask(
            title = "",
            description = "",
            id = taskId,
            isCompleted = false,
            date = Clock.System.now().toString(),
            isEditing = true,
        )
        localDataSource.upsert(task)
        saveTasksToNetwork()
    }

    override suspend fun create(title: String, description: String): String {
        val taskId = withContext(dispatcher) {
            createTaskId()
        }
        val task = LocalTask(
            title = title,
            description = description,
            id = taskId,
            isCompleted = false,
            date = Clock.System.now().toString(),
            isEditing = true,
        )
        localDataSource.upsert(task)
        saveTasksToNetwork()
        return taskId
    }

    override suspend fun complete(taskId: String) {
        localDataSource.updateCompleted(taskId, true)
        saveTasksToNetwork()
    }

    override suspend fun upsert(task: LocalTask) {
        localDataSource.upsert(task)
        saveTasksToNetwork()
    }

    override suspend fun refresh() {
        val networkTasks = networkDataSource.loadTasks()
        localDataSource.deleteAll()
        val localTasks = withContext(dispatcher) {
            networkTasks.toLocal()
        }
        localDataSource.upsertAll(networkTasks.toLocal())
    }

    override fun delete(task: LocalTask) {
        scope.launch {
            withContext(dispatcher) {
                localDataSource.delete(task.id)
                saveTasksToNetwork()
            }
        }
    }

    override fun clear() {
        scope.launch {
            withContext(dispatcher) {
                localDataSource.deleteAll()
            }
        }
    }

    private fun saveTasksToNetwork() {
        scope.launch {
            val localTasks = localDataSource.observeAll().first()
            val networkTasks = withContext(dispatcher) {
                localTasks.toNetwork()
            }
            networkDataSource.saveTasks(networkTasks)
        }
    }

    // This method might be computationally expensive
    private fun createTaskId(): String {
        return UUID.randomUUID().toString()
    }
}
