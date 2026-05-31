package com.keelim.core.data.source.cache

import com.keelim.shared.data.database.dao.NetworkCacheDao
import com.keelim.shared.data.database.model.NetworkCache
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class CacheRepositoryImplTest : FunSpec({

    lateinit var dao: NetworkCacheDao
    lateinit var client: HttpClient
    lateinit var repository: CacheRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        dao = mockk(relaxed = true)
        client = mockk(relaxed = true)
        repository = CacheRepositoryImpl(dao, client)
    }

    test("getResponse는 유효한 캐시가 있으면 네트워크 호출 없이 캐시 json을 반환한다") {
        runTest(testDispatcher) {
            val cached = mockk<NetworkCache>()
            every { cached.timestamp } returns System.currentTimeMillis()
            every { cached.json } returns "{\"cached\":true}"
            coEvery { dao.getCache("https://api/x") } returns cached

            val result = repository.getResponse("https://api/x", enforce = false)

            result shouldBe "{\"cached\":true}"
            coVerify(exactly = 0) { dao.upsertCache(any()) }
        }
    }
})
