package com.keelim.core.data.source

import com.keelim.shared.data.database.dao.ShortenedUrlDao
import com.keelim.shared.data.database.model.ShortenedUrlEntity
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class ShortenedUrlRepositoryImplTest : FunSpec({

    lateinit var dao: ShortenedUrlDao
    lateinit var repository: ShortenedUrlRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        repository = ShortenedUrlRepositoryImpl(dao)
    }

    test("getAll은 DAO Flow를 그대로 위임한다") {
        val expected = flowOf(emptyList<ShortenedUrlEntity>())
        every { dao.getAll() } returns expected

        repository.getAll() shouldBe expected
    }

    test("getMostClicked는 limit을 DAO에 전달한다") {
        val expected = flowOf(emptyList<ShortenedUrlEntity>())
        every { dao.getMostClicked(5) } returns expected

        repository.getMostClicked(5) shouldBe expected
        verify { dao.getMostClicked(5) }
    }

    test("getById는 DAO 결과를 반환한다") {
        runTest(testDispatcher) {
            val entity = mockk<ShortenedUrlEntity>()
            coEvery { dao.getById(7L) } returns entity

            repository.getById(7L) shouldBe entity
        }
    }

    test("getByShortCode는 DAO 결과를 반환한다") {
        runTest(testDispatcher) {
            val entity = mockk<ShortenedUrlEntity>()
            coEvery { dao.getByShortCode("abc") } returns entity

            repository.getByShortCode("abc") shouldBe entity
        }
    }

    test("insert는 DAO가 반환한 id를 그대로 반환한다") {
        runTest(testDispatcher) {
            val entity = mockk<ShortenedUrlEntity>()
            coEvery { dao.insert(entity) } returns 42L

            repository.insert(entity) shouldBe 42L
            coVerify { dao.insert(entity) }
        }
    }

    test("update는 DAO update에 위임한다") {
        runTest(testDispatcher) {
            val entity = mockk<ShortenedUrlEntity>()

            repository.update(entity)

            coVerify { dao.update(entity) }
        }
    }

    test("delete는 DAO delete에 위임한다") {
        runTest(testDispatcher) {
            val entity = mockk<ShortenedUrlEntity>()

            repository.delete(entity)

            coVerify { dao.delete(entity) }
        }
    }

    test("incrementClickCount는 id와 timestamp를 DAO에 전달한다") {
        runTest(testDispatcher) {
            repository.incrementClickCount(3L, 1_000L)

            coVerify { dao.incrementClickCount(3L, 1_000L) }
        }
    }

    test("deleteExpired는 currentTime을 DAO에 전달한다") {
        runTest(testDispatcher) {
            repository.deleteExpired(2_000L)

            coVerify { dao.deleteExpired(2_000L) }
        }
    }
})
