package com.keelim.core.data.source

import com.keelim.core.data.model.NetworkTask
import com.keelim.shared.data.database.dao.TaskDao
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldNotBeBlank
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultTaskRepositoryImplTest : FunSpec({

    lateinit var localDataSource: TaskDao
    lateinit var networkDataSource: TaskNetworkDataSource
    lateinit var repository: DefaultTaskRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        localDataSource = mockk(relaxed = true)
        networkDataSource = mockk(relaxed = true)
        every { localDataSource.observeAll() } returns flowOf(emptyList())
        repository = DefaultTaskRepositoryImpl(
            localDataSource = localDataSource,
            networkDataSource = networkDataSource,
            dispatcher = testDispatcher,
            scope = CoroutineScope(testDispatcher),
        )
    }

    test("create(title, description)는 비어있지 않은 taskId를 반환하고 로컬에 upsert한 뒤 네트워크에 저장한다") {
        runTest(testDispatcher) {
            val taskId = repository.create(title = "할 일", description = "설명")

            taskId.shouldNotBeBlank()
            advanceUntilIdle()
            coVerify { localDataSource.upsert(any()) }
            coVerify { networkDataSource.saveTasks(any()) }
        }
    }

    test("complete는 로컬 updateCompleted(taskId, true)를 호출하고 네트워크에 저장한다") {
        runTest(testDispatcher) {
            repository.complete("task-1")

            advanceUntilIdle()
            coVerify { localDataSource.updateCompleted("task-1", true) }
            coVerify { networkDataSource.saveTasks(any()) }
        }
    }

    test("refresh()는 네트워크에서 데이터를 읽어와 로컬을 비우고 새로 upsert한다") {
        runTest(testDispatcher) {
            coEvery { networkDataSource.loadTasks() } returns emptyList<NetworkTask>()

            repository.refresh()

            advanceUntilIdle()
            coVerify { networkDataSource.loadTasks() }
            coVerify { localDataSource.deleteAll() }
            coVerify { localDataSource.upsertAll(any()) }
        }
    }

    test("delete는 로컬에서 삭제하고 네트워크에 저장한다") {
        runTest(testDispatcher) {
            val task = com.keelim.model.LocalTask(
                id = "task-2",
                title = "제목",
                description = "설명",
                isCompleted = false,
                date = "2026-06-14",
                isEditing = false
            )

            repository.delete(task)

            advanceUntilIdle()
            coVerify { localDataSource.delete("task-2") }
            coVerify { networkDataSource.saveTasks(any()) }
        }
    }

    test("clear는 로컬의 모든 데이터를 지운다") {
        runTest(testDispatcher) {
            repository.clear()

            advanceUntilIdle()
            coVerify { localDataSource.deleteAll() }
        }
    }
})
