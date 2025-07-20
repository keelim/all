package com.keelim.core.data.source.finance

import com.keelim.core.model.finance.FinanceCategory
import com.keelim.core.model.finance.FinanceRssItem
import com.keelim.core.model.finance.FinanceSource
import com.keelim.data.repository.FinanceRssRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Instant
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import timber.log.Timber
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FinanceRssRepositoryImpl @Inject constructor() : FinanceRssRepository {

    // 메모리 캐시
    private val cache = mutableMapOf<String, CachedRssData>()
    private val cacheExpiryTime = 5 * 60 * 1000L // 5분

    private val defaultSources = listOf(
        FinanceSource(
            name = "한국경제",
            url = "https://www.hankyung.com/feed/economy",
            category = FinanceCategory.ECONOMY
        ),
        FinanceSource(
            name = "매일경제",
            url = "https://www.mk.co.kr/rss/30000001/",
            category = FinanceCategory.ECONOMY
        ),
        FinanceSource(
            name = "서울경제",
            url = "https://www.sedaily.com/RSS/S11.xml",
            category = FinanceCategory.STOCK
        ),
        FinanceSource(
            name = "이데일리",
            url = "https://www.edaily.co.kr/rss/rss_01.xml",
            category = FinanceCategory.STOCK
        ),
        FinanceSource(
            name = "비트코인뉴스",
            url = "https://www.bitcoinnews.com/feed/",
            category = FinanceCategory.CRYPTO
        ),
        FinanceSource(
            name = "코인데스크",
            url = "https://www.coindesk.com/arc/outboundfeeds/rss/",
            category = FinanceCategory.CRYPTO
        ),
        FinanceSource(
            name = "부동산뉴스",
            url = "https://www.reb.or.kr/rss/rss_news.xml",
            category = FinanceCategory.REAL_ESTATE
        )
    )

    override fun getRssItems(sources: List<FinanceSource>): Flow<List<FinanceRssItem>> = flow {
        val allItems = mutableListOf<FinanceRssItem>()

        sources.filter { it.isEnabled }.forEach { source ->
            try {
                val items = getRssItemsWithCache(source)
                allItems.addAll(items)
            } catch (e: Exception) {
                Timber.e(e, "RSS fetch error for ${source.name}: ${e.message}")
            }
        }

        // 최신순으로 정렬
        allItems.sortByDescending { it.pubDate }
        emit(allItems)
    }.flowOn(Dispatchers.IO)

    private suspend fun getRssItemsWithCache(source: FinanceSource): List<FinanceRssItem> {
        val cacheKey = source.url
        val cachedData = cache[cacheKey]

        // 캐시가 유효한 경우 캐시된 데이터 반환
        if (cachedData != null && !isCacheExpired(cachedData.timestamp)) {
            Timber.d("Using cached data for ${source.name}")
            return cachedData.items
        }

        // 캐시가 없거나 만료된 경우 네트워크에서 새로 가져오기
        return try {
            val items = fetchRssFromUrl(source.url, source.name, source.category)
            // 캐시에 저장
            cache[cacheKey] = CachedRssData(items, System.currentTimeMillis())
            Timber.d("Fetched and cached new data for ${source.name}: ${items.size} items")
            items
        } catch (e: Exception) {
            // 네트워크 실패 시 캐시된 데이터가 있으면 반환
            cachedData?.items?.let { cachedItems ->
                Timber.w("Network failed for ${source.name}, using stale cache")
                return cachedItems
            } ?: emptyList()
        }
    }

    private fun isCacheExpired(timestamp: Long): Boolean {
        return System.currentTimeMillis() - timestamp > cacheExpiryTime
    }

    private suspend fun fetchRssFromUrl(
        url: String,
        sourceName: String,
        category: FinanceCategory
    ): List<FinanceRssItem> {
        return try {
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()

            val connection = URL(url).openConnection()
            connection.connectTimeout = 10000
            connection.readTimeout = 10000

            parser.setInput(connection.getInputStream(), null)

            val items = mutableListOf<FinanceRssItem>()
            var eventType = parser.eventType
            var currentItem: MutableMap<String, String>? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "item" -> {
                                currentItem = mutableMapOf()
                            }
                            "title", "description", "link", "pubDate", "category" -> {
                                if (currentItem != null) {
                                    currentItem[parser.name] = parser.nextText()
                                }
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item" && currentItem != null) {
                            items.add(
                                FinanceRssItem(
                                    title = currentItem["title"] ?: "",
                                    description = currentItem["description"] ?: "",
                                    link = currentItem["link"] ?: "",
                                    pubDate = parseDate(currentItem["pubDate"]),
                                    category = currentItem["category"] ?: category.displayName,
                                    source = sourceName
                                )
                            )
                            currentItem = null
                        }
                    }
                }
                eventType = parser.next()
            }

            items
        } catch (e: Exception) {
            Timber.e(e, "Error fetching RSS from $url")
            emptyList()
        }
    }

    private fun parseDate(dateString: String?): Instant? {
        if (dateString.isNullOrBlank()) return null

        return try {
            // 간단한 날짜 파싱 (실제로는 더 정교한 파싱이 필요)
            // RFC 822 형식: "Wed, 02 Oct 2002 15:00:00 +0200"
            val pattern = "EEE, dd MMM yyyy HH:mm:ss Z"
            val formatter = java.time.format.DateTimeFormatter.ofPattern(pattern)
            val zonedDateTime = java.time.ZonedDateTime.parse(dateString, formatter)
            Instant.fromEpochMilliseconds(zonedDateTime.toInstant().toEpochMilli())
        } catch (e: Exception) {
            null
        }
    }

    override fun getSources(): List<FinanceSource> = defaultSources

    // 캐시 무효화 메서드
    override fun clearCache() {
        cache.clear()
        Timber.d("Finance RSS cache cleared")
    }

    // 특정 소스의 캐시만 무효화
    override fun invalidateCacheForSource(sourceUrl: String) {
        cache.remove(sourceUrl)
        Timber.d("Cache invalidated for source: $sourceUrl")
    }

    // 캐시 상태 확인
    override fun getCacheInfo(): Map<String, Long> {
        return cache.mapValues { it.value.timestamp }
    }
}

// 캐시 데이터 클래스
private data class CachedRssData(
    val items: List<FinanceRssItem>,
    val timestamp: Long
)
