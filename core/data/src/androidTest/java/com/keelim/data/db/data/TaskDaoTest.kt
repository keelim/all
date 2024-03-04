package com.keelim.data.db.data

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TaskDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var appDatabase: com.keelim.core.database.MyGradeAppDatabase

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
        appDatabase.taskDao().upsert(task)
        val tasks = appDatabase.taskDao().observeAll().first()
        assertEquals(1, tasks.size)
        assertEquals(task, tasks[0])
    }
}
