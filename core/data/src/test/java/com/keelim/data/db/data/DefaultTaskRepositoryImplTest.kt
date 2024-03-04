package com.keelim.data.db.data

import com.keelim.core.database.model.LocalTask
import com.keelim.data.db.data.source.local.FakeTaskDao
import com.keelim.data.source.DefaultTaskRepositoryImpl
import com.keelim.data.source.network.NetworkTask
import com.keelim.data.source.network.TaskNetworkDataSource
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import org.junit.Test

class DefaultTaskRepositoryImplTest {

    private var testDispatcher = UnconfinedTestDispatcher()
    private var testScope = TestScope(testDispatcher)
    private val systemTZ = TimeZone.currentSystemDefault()

    private val localTasks = listOf(
        LocalTask(
            id = "1",
            title = "title1",
            description = "description1",
            isCompleted = false,
            date = Clock.System.now().toString(),
            isEditing = false,
        ),
        LocalTask(
            id = "2",
            title = "title2",
            description = "description2",
            isCompleted = true,
            date = Clock.System.now().plus(1, DateTimeUnit.DAY, systemTZ).toString(),
            isEditing = true,
        ),
    )

    private val localDataSource = FakeTaskDao(localTasks)
    private val networkDataSource = TaskNetworkDataSource()
    private val taskRepository = DefaultTaskRepositoryImpl(
        localDataSource = localDataSource,
        networkDataSource = networkDataSource,
        dispatcher = testDispatcher,
        scope = testScope,
    )

//    @Test
//    fun observeAll_exposesLocalData() = runTest {
//        val tasks = taskRepository.observeAll().first()
//        assertEquals(localTasks.toExternal(), tasks)
//    }

    @Test
    fun onTaskCreation_localAndNetworkAreUpdated() = testScope.runTest {
        val newTaskId = taskRepository.create(
            localTasks[0].title,
            localTasks[0].description,
        )

        val localTasks = localDataSource.observeAll().first()
        assertEquals(true, localTasks.map { it.id }.contains(newTaskId))

        val networkTasks = networkDataSource.loadTasks()
        assertEquals(true, networkTasks.map { it.id }.contains(newTaskId))
    }

    fun onTaskCompletion_localAndNetworkAreUpdated() = testScope.runTest {
        taskRepository.complete("1")

        val localTasks = localDataSource.observeAll().first()
        val isLocalTaskComplete = localTasks.firstOrNull { it.id == "1" }?.isCompleted
        assertEquals(true, isLocalTaskComplete)

        val networkTasks = networkDataSource.loadTasks()
        val isNetworkTaskComplete =
            networkTasks.firstOrNull { it.id == "1" }?.status == NetworkTask.TaskStatus.COMPLETE
        assertEquals(true, isNetworkTaskComplete)
    }

//    @Test
//    fun onRefresh_localIsEqualToNetwork() = runTest {
//        val networkTasks = listOf(
//            NetworkTask(id = "3", title = "title3", shortDescription = "desc3"),
//            NetworkTask(id = "4", title = "title4", shortDescription = "desc4"),
//        )
//        networkDataSource.saveTasks(networkTasks)
//
//        taskRepository.refresh()
//
//        assertEquals(networkTasks.toLocal(), localDataSource.observeAll().first())
//    }
}
