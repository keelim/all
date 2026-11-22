package com.keelim.core.data.source

import com.keelim.data.repository.ArduconDataSource
import com.keelim.model.DeepLink
import com.keelim.model.UsageStat
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class ArduconRepositoryImplTest : FunSpec({

    lateinit var local: ArduconDataSource
    lateinit var repository: ArduconRepositoryImpl
    val testDispatcher = StandardTestDispatcher()

    extension(MainDispatcherRule(testDispatcher))

    beforeTest {
        local = mockk(relaxed = true)
        repository = ArduconRepositoryImpl(local, testDispatcher)
    }

    test("getDeepLinkUrls는 키워드가 비어있으면 전체 목록을 조회한다") {
        val all = flowOf(emptyList<DeepLink>())
        every { local.getDeepLinkUrls() } returns all

        repository.getDeepLinkUrls("") shouldBe all
        verify { local.getDeepLinkUrls() }
    }

    test("getDeepLinkUrls는 키워드가 있으면 필터 조회한다") {
        val filtered = flowOf(emptyList<DeepLink>())
        every { local.getDeepLinkUrlsFiltered("kw") } returns filtered

        repository.getDeepLinkUrls("kw") shouldBe filtered
        verify { local.getDeepLinkUrlsFiltered("kw") }
    }

    test("insertDeepLinkUrl은 dataSource에 위임한다") {
        runTest(testDispatcher) {
            val deepLink = mockk<DeepLink>()

            repository.insertDeepLinkUrl(deepLink)

            coVerify { local.insertDeepLinkUrl(deepLink) }
        }
    }

    test("getTopUsedLinks는 dataSource 결과를 방출한다") {
        runTest(testDispatcher) {
            val links = listOf(mockk<DeepLink>())
            coEvery { local.getTopUsedLinks(3) } returns links

            repository.getTopUsedLinks(3).first() shouldBe links
        }
    }

    test("getDailyUsageStats는 dataSource 결과를 방출한다") {
        runTest(testDispatcher) {
            val stats = listOf(mockk<UsageStat>())
            coEvery { local.getDailyUsageStats(7) } returns stats

            repository.getDailyUsageStats(7).first() shouldBe stats
        }
    }
})
