package com.keelim.core.model.finance

import kotlinx.datetime.Instant

data class FinanceRssItem(
    val title: String = "",
    val description: String = "",
    val link: String = "",
    val pubDate: Instant? = null,
    val category: String = "",
    val source: String = "",
) {
    val isRecent: Boolean = pubDate?.let { 
        val now = Instant.fromEpochMilliseconds(System.currentTimeMillis())
        (now.epochSeconds - it.epochSeconds) < 24 * 60 * 60 // 24시간 이내
    } ?: false
}

data class FinanceFilter(
    val category: FinanceCategory = FinanceCategory.ALL,
    val source: String = "",
)

enum class FinanceCategory(val displayName: String) {
    ALL("전체"),
    STOCK("주식"),
    CRYPTO("암호화폐"),
    FOREX("외환"),
    ECONOMY("경제"),
    REAL_ESTATE("부동산"),
}

data class FinanceSource(
    val name: String,
    val url: String,
    val category: FinanceCategory,
    val isEnabled: Boolean = true,
) 