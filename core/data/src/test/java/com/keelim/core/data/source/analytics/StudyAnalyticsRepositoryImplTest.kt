package com.keelim.core.data.source.analytics

import com.keelim.shared.data.database.dao.StudySessionDao
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
class StudyAnalyticsRepositoryImplTest : FunSpec({

    lateinit var dao: StudySessionDao
    lateinit var repository: StudyAnalyticsRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = StudyAnalyticsRepositoryImpl(dao)
    }

    test("getTotalStudySeconds는 DAO가 null을 주면 0으로 보정한다") {
        runTest(testDispatcher) {
            every { dao.getTotalStudySeconds() } returns flowOf<Int?>(null)

            repository.getTotalStudySeconds().first() shouldBe 0
        }
    }

    test("getTotalStudySeconds는 DAO 값을 그대로 전달한다") {
        runTest(testDispatcher) {
            every { dao.getTotalStudySeconds() } returns flowOf<Int?>(3600)

            repository.getTotalStudySeconds().first() shouldBe 3600
        }
    }

    test("getStudyDaysCount는 DAO가 null을 주면 0으로 보정한다") {
        runTest(testDispatcher) {
            every { dao.getStudyDaysCount() } returns flowOf<Int?>(null)

            repository.getStudyDaysCount().first() shouldBe 0
        }
    }

    test("recordSession은 StudySession을 만들어 upsert한다") {
        runTest(testDispatcher) {
            repository.recordSession(subject = "영어", durationSeconds = 1500)

            coVerify { dao.upsert(any()) }
        }
    }
})
