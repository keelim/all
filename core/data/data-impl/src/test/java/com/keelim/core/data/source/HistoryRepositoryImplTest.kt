package com.keelim.core.data.source

import com.keelim.shared.data.database.dao.HistoryDao
import com.keelim.shared.data.database.dao.TimerHistoryDao
import com.keelim.shared.data.database.model.SimpleHistory
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.coEvery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryRepositoryImplTest : FunSpec({

    lateinit var historyDao: HistoryDao
    lateinit var timerHistoryDao: TimerHistoryDao
    lateinit var repository: HistoryRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        historyDao = mockk(relaxed = true)
        timerHistoryDao = mockk(relaxed = true)
        repository = HistoryRepositoryImpl(
            localDataSource = historyDao,
            timerHistoryDataSource = timerHistoryDao,
            io = testDispatcher,
            default = testDispatcher,
            scope = CoroutineScope(testDispatcher),
        )
    }

    test("create는 point를 gradeRank/totalRank로 파싱하고 upsert 후 true를 반환한다") {
        runTest(testDispatcher) {
            val slot = slot<SimpleHistory>()
            coEvery { historyDao.upsertSimpleHistory(capture(slot)) } returns Unit

            val result = repository.create(subject = "수학", grade = "A", point = "1 / 10")

            result shouldBe true
            coVerify { historyDao.upsertSimpleHistory(any()) }
            slot.captured.subject shouldBe "수학"
            slot.captured.grade shouldBe "A"
            slot.captured.gradeRank shouldBe 1
            slot.captured.totalRank shouldBe 10
        }
    }

    test("create는 point 형식이 잘못되면 upsert 없이 false를 반환한다") {
        runTest(testDispatcher) {
            val result = repository.create(subject = "수학", grade = "A", point = "잘못된값")

            result shouldBe false
            coVerify(exactly = 0) { historyDao.upsertSimpleHistory(any()) }
        }
    }

    test("complete는 historyId와 grade로 updateCompleted를 호출한다") {
        runTest(testDispatcher) {
            repository.complete(historyId = "id-1", grade = "B")

            coVerify { historyDao.updateCompleted("id-1", "B") }
        }
    }

    test("completedTimerHistory는 timerHistoryDao updateCompleted에 위임한다") {
        runTest(testDispatcher) {
            repository.completedTimerHistory(historyId = 5)

            coVerify { timerHistoryDao.updateCompleted(5) }
        }
    }

    test("deleteAllTimerHistories는 timerHistoryDao deleteAll에 위임한다") {
        runTest(testDispatcher) {
            repository.deleteAllTimerHistories()

            coVerify { timerHistoryDao.deleteAll() }
        }
    }

    test("refresh는 localDataSource deleteAll에 위임한다") {
        runTest(testDispatcher) {
            repository.refresh()

            coVerify { historyDao.deleteAll() }
        }
    }
})
