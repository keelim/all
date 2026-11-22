package com.keelim.core.data.source

import com.keelim.shared.data.database.dao.Base64Dao
import com.keelim.shared.data.database.model.Base64History
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class Base64RepositoryImplTest : FunSpec({

    lateinit var dao: Base64Dao
    lateinit var repository: Base64RepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = Base64RepositoryImpl(dao)
    }

    test("getAllHistory는 DAO의 getAll Flow를 그대로 위임한다") {
        val expected = flowOf(emptyList<Base64History>())
        every { dao.getAll() } returns expected

        repository.getAllHistory() shouldBe expected
    }

    test("insertHistory는 입력값으로 Base64History를 만들어 insert를 호출한다") {
        runTest(testDispatcher) {
            val captured = slot<Base64History>()
            coEvery { dao.insert(capture(captured)) } returns Unit

            repository.insertHistory(text = "hello", isEncoded = true)

            coVerify { dao.insert(any()) }
            captured.captured.text shouldBe "hello"
            captured.captured.isEncoded shouldBe true
        }
    }

    test("deleteHistory는 DAO delete에 위임한다") {
        runTest(testDispatcher) {
            val history = Base64History(text = "x", isEncoded = false)

            repository.deleteHistory(history)

            coVerify { dao.delete(history) }
        }
    }

    test("deleteAllHistory는 DAO deleteAll에 위임한다") {
        runTest(testDispatcher) {
            repository.deleteAllHistory()

            coVerify { dao.deleteAll() }
        }
    }
})
