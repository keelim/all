package com.keelim.data.repository

import com.keelim.core.model.finance.FinanceRssItem
import com.keelim.core.model.finance.FinanceSource
import kotlinx.coroutines.flow.Flow

interface FinanceRssRepository {
    fun getRssItems(sources: List<FinanceSource>): Flow<List<FinanceRssItem>>
    fun getSources(): List<FinanceSource>
    
    // 캐시 관련 메서드들
    fun clearCache()
    fun invalidateCacheForSource(sourceUrl: String)
    fun getCacheInfo(): Map<String, Long>
} 