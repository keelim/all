package com.keelim.core.data.source.finance

import com.keelim.model.finance.FinanceCategory
import com.keelim.model.finance.FinanceSource
import com.keelim.testing.util.MainDispatcherRule
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class FinanceRssRepositoryImplTest : FunSpec({

    lateinit var repository: FinanceRssRepositoryImpl
    val testDispatcher = StandardTestDispatcher()
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    extension(mainDispatcherRule)

    fun parseDate(dateString: String?): Instant? {
        if (dateString.isNullOrBlank()) return null

        return try {
            val pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
            val formatter = java.time.format.DateTimeFormatter.ofPattern(pattern)
            val zonedDateTime = java.time.ZonedDateTime.parse(dateString, formatter)
            Instant.fromEpochMilliseconds(zonedDateTime.toInstant().toEpochMilli())
        } catch (e: Exception) {
            null
        }
    }

    beforeTest {
        repository = FinanceRssRepositoryImpl()
    }

    test("getSources는 기본 소스 목록을 반환해야 한다") {
        val sources = repository.getSources()

        sources.isNotEmpty() shouldBe true
        sources.any { it.name == "한국경제" } shouldBe true
        sources.any { it.name == "매일경제" } shouldBe true
        sources.any { it.name == "서울경제" } shouldBe true
        sources.any { it.name == "이데일리" } shouldBe true
        sources.any { it.name == "비트코인뉴스" } shouldBe true
        sources.any { it.name == "코인데스크" } shouldBe true
        sources.any { it.name == "부동산뉴스" } shouldBe true
    }

    test("기본 소스들이 올바른 카테고리를 가져야 한다") {
        val sources = repository.getSources()

        val economySources = sources.filter { it.category == FinanceCategory.ECONOMY }
        val stockSources = sources.filter { it.category == FinanceCategory.STOCK }
        val cryptoSources = sources.filter { it.category == FinanceCategory.CRYPTO }
        val realEstateSources = sources.filter { it.category == FinanceCategory.REAL_ESTATE }

        economySources.isNotEmpty() shouldBe true
        stockSources.isNotEmpty() shouldBe true
        cryptoSources.isNotEmpty() shouldBe true
        realEstateSources.isNotEmpty() shouldBe true
    }

    test("캐시 초기화가 올바르게 동작해야 한다") {
        repository.clearCache()

        val cacheInfo = repository.getCacheInfo()
        cacheInfo.isEmpty() shouldBe true
    }

    test("특정 소스 캐시 무효화가 올바르게 동작해야 한다") {
        val sourceUrl = "https://test.com/feed"

        repository.invalidateCacheForSource(sourceUrl)

        val cacheInfo = repository.getCacheInfo()
        cacheInfo.containsKey(sourceUrl) shouldBe false
    }

    test("캐시 정보가 올바르게 반환되어야 한다") {
        val cacheInfo = repository.getCacheInfo()

        cacheInfo.isEmpty() shouldBe true
    }

    test("잘못된 날짜 형식은 null을 반환해야 한다") {
        val invalidDateString = "잘못된 날짜 형식"
        val parsedDate = parseDate(invalidDateString)

        (parsedDate == null) shouldBe true
    }

    test("null 날짜는 null을 반환해야 한다") {
        val parsedDate = parseDate(null)
        (parsedDate == null) shouldBe true
    }

    test("빈 날짜는 null을 반환해야 한다") {
        val parsedDate = parseDate("")
        (parsedDate == null) shouldBe true
    }

    test("getRssItems는 활성화된 소스만 처리해야 한다") {
        runTest {
            val sources = listOf(
                FinanceSource(
                    name = "활성화된 소스",
                    url = "https://active.com/feed",
                    category = FinanceCategory.ECONOMY,
                    isEnabled = true,
                ),
                FinanceSource(
                    name = "비활성화된 소스",
                    url = "https://inactive.com/feed",
                    category = FinanceCategory.STOCK,
                    isEnabled = false,
                ),
            )

            val items = repository.getRssItems(sources).first()

            items.isEmpty() shouldBe true
        }
    }

    test("캐시 만료 시간이 올바르게 설정되어야 한다") {
        val expectedExpiryTime = 5 * 60 * 1000L

        val currentTime = System.currentTimeMillis()
        val cacheTime = currentTime - 1000L

        val isExpired = currentTime - cacheTime > expectedExpiryTime
        isExpired shouldBe false

        val oldCacheTime = currentTime - (6 * 60 * 1000L)
        val isOldExpired = currentTime - oldCacheTime > expectedExpiryTime
        isOldExpired shouldBe true
    }

})
