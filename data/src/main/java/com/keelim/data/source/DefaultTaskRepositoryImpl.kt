package com.keelim.data.source

import com.keelim.data.db.dao.TaskDao
import com.keelim.data.di.ApplicationScope
import com.keelim.data.di.DefaultDispatcher
import com.keelim.data.source.local.LocalTask
import com.keelim.data.source.network.TaskNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.util.UUID
import javax.inject.Inject

class DefaultTaskRepositoryImpl @Inject constructor(
    private val localDataSource: TaskDao,
    private val networkDataSource: TaskNetworkDataSource,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : DefaultTaskRepository {

    override fun observeAll(): Flow<List<LocalTask>> {
        return localDataSource.observeAll()
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
            date = Clock.System.now().toString()
        )
        localDataSource.upsert(task)
        saveTasksToNetwork()
        return taskId
    }

    override suspend fun complete(taskId: String) {
        localDataSource.updateCompleted(taskId, true)
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

    private suspend fun saveTasksToNetwork() {
        scope.launch {
            val localTasks = localDataSource.observeAll().first()
            val networkTasks = withContext(dispatcher) {
                localTasks.toNetwork()
            }
            networkDataSource.saveTasks(networkTasks)
        }
    }

    // This method might be computationally expensive
    private fun createTaskId() : String {
        return UUID.randomUUID().toString()
    }
}
