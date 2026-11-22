package com.keelim.core.data.source.note

import com.keelim.shared.data.database.dao.NoteDao
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NoteRepositoryImplTest : FunSpec({

    lateinit var dao: NoteDao
    lateinit var repository: NoteRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = NoteRepositoryImpl(dao, testDispatcher)
    }

    test("getNoteList는 빈 DAO 스트림을 Result.success(빈 목록)으로 감싼다") {
        runTest(testDispatcher) {
            every { dao.getNotes() } returns flowOf(emptyList())

            val result = repository.getNoteList().first()

            result.isSuccess shouldBe true
            result.getOrNull() shouldBe emptyList()
        }
    }

    test("getNoteDetail은 DAO 예외를 Result.failure로 감싼다") {
        runTest(testDispatcher) {
            coEvery { dao.getNoteDetail(99) } throws RuntimeException("not found")

            val result = repository.getNoteDetail(99)

            result.isFailure shouldBe true
        }
    }
})
