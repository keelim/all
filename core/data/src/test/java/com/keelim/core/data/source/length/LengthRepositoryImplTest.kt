package com.keelim.core.data.source.length

import com.keelim.shared.data.database.dao.LengthRecordDao
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class LengthRepositoryImplTest : FunSpec({

    lateinit var dao: LengthRecordDao
    lateinit var repository: LengthRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = LengthRepositoryImpl(dao)
    }

    test("getAllRecords는 DAO 스트림을 도메인으로 매핑한다 (빈 목록)") {
        runTest(testDispatcher) {
            every { dao.getAll() } returns flowOf(emptyList())

            repository.getAllRecords().first() shouldBe emptyList()
        }
    }

    test("deleteRecord는 date를 DAO에 전달한다") {
        runTest(testDispatcher) {
            repository.deleteRecord("2026-01-01")

            coVerify { dao.deleteByDate("2026-01-01") }
        }
    }
})
