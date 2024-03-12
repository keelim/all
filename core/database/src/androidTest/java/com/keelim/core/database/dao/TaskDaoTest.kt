package com.keelim.core.database.dao

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class TaskDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var taskDao: TaskDao

    @Test
    fun insertTaskAndGetTasks() = runTest {
        val task = com.keelim.core.database.model.LocalTask(
            title = "title",
            description = "description",
            id = "id",
            isCompleted = false,
            date = "2021-03-27T02:16:20",
            isEditing = true,
        )
        taskDao.upsert(task)
        val tasks = taskDao.observeAll().first()
        assertEquals(1, tasks.size)
        assertEquals(task, tasks[0])
    }
}
