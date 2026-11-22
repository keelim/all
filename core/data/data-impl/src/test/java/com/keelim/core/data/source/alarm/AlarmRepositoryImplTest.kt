package com.keelim.core.data.source.alarm

import com.keelim.shared.data.database.dao.AlarmDao
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmRepositoryImplTest : FunSpec({

    lateinit var dao: AlarmDao
    lateinit var repository: AlarmRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = AlarmRepositoryImpl(dao, testDispatcher)
    }

    test("insertAlarm은 성공 시 upsert 후 true를 반환한다") {
        runTest(testDispatcher) {
            repository.insertAlarm(title = "기상", subTitle = "7시") shouldBe true

            coVerify { dao.upsert(any()) }
        }
    }

    test("insertAlarm은 DAO 예외 시 false를 반환한다") {
        runTest(testDispatcher) {
            coEvery { dao.upsert(any()) } throws RuntimeException("db error")

            repository.insertAlarm(title = "기상", subTitle = "7시") shouldBe false
        }
    }

    test("getAlarms는 DAO 스트림을 도메인으로 매핑한다 (빈 목록)") {
        runTest(testDispatcher) {
            every { dao.getAllAlarms() } returns flowOf(emptyList())

            repository.getAlarms().first() shouldBe emptyList()
        }
    }
})
