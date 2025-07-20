package com.keelim.data.repository

import com.keelim.core.model.finance.FinanceRssItem
import com.keelim.core.model.finance.FinanceSource
import kotlinx.coroutines.flow.Flow

interface FinanceRssRepository {
    fun getRssItems(sources: List<FinanceSource>): Flow<List<FinanceRssItem>>
    fun getSources(): List<FinanceSource>
} 