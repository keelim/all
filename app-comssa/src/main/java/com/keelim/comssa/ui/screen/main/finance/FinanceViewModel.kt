package com.keelim.comssa.ui.screen.main.finance

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.core.model.finance.FinanceRssItem
import com.keelim.data.repository.FinanceRssRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for finance RSS feed management following MVVM architecture
 * Handles filtering, caching, and state management for financial news items
 */
@Stable
@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val financeRssRepository: FinanceRssRepository,
) : ViewModel() {

    // Filter instances for different financial categories
    private val filterAll = FilterAll()
    private val filterStock = FilterStock()
    private val filterCrypto = FilterCrypto()
    private val filterForex = FilterForex()
    private val filterEconomy = FilterEconomy()
    private val filterRealEstate = FilterRealEstate()

    // Internal state flows for filtering and refresh management
    private val categoryFilter = MutableStateFlow<FabButtonItem>(filterAll)
    private val sourceFilter = MutableStateFlow<String>("")
    private val refreshTrigger = MutableStateFlow(0)

    /**
     * StateFlow representing filtered and processed RSS items
     * Combines category, source filtering with refresh capabilities
     */
    val items = combine(
        refreshTrigger,
        categoryFilter,
        sourceFilter
    ) { _, category, source ->
        financeRssRepository.getRssItems(financeRssRepository.getSources())
            .map { items ->
                filterItems(items, category, source)
            }
    }.flatMapLatest { it }
        .flowOn(Dispatchers.Default)
        .asSealedUiState(emptyToLoading = false)
        .catch { throwable ->
            Timber.e(throwable, "Error loading finance RSS items")
            emitAll(emptyFlow())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SealedUiState.loading()
        )

    /**
     * List of available filter buttons for UI display
     */
    val filterButtons = listOf(
        filterAll,
        filterStock,
        filterCrypto,
        filterForex,
        filterEconomy,
        filterRealEstate,
    )

    // Constants for category filtering - TODO: Move to string resources
    private companion object {
        const val CATEGORY_STOCK_KO = "주식"
        const val CATEGORY_STOCK_EN = "Stock"
        const val CATEGORY_CRYPTO_KO = "암호화폐"
        const val CATEGORY_CRYPTO_EN = "Crypto"
        const val CATEGORY_FOREX_KO = "외환"
        const val CATEGORY_FOREX_EN = "Forex"
        const val CATEGORY_ECONOMY_KO = "경제"
        const val CATEGORY_ECONOMY_EN = "Economy"
        const val CATEGORY_REAL_ESTATE_KO = "부동산"
        const val CATEGORY_REAL_ESTATE_EN = "Real Estate"
    }

    /**
     * Filters RSS items based on category and source filters
     * 
     * @param items List of RSS items to filter
     * @param categoryFilter Current category filter
     * @param sourceFilter Current source filter
     * @return Filtered list of RSS items
     */
    private fun filterItems(
        items: List<FinanceRssItem>,
        categoryFilter: FabButtonItem,
        sourceFilter: String
    ): List<FinanceRssItem> {
        var filteredItems = when (categoryFilter) {
            filterAll -> items
            filterStock -> items.filter { 
                it.category.contains(CATEGORY_STOCK_KO) || it.category.contains(CATEGORY_STOCK_EN) 
            }
            filterCrypto -> items.filter { 
                it.category.contains(CATEGORY_CRYPTO_KO) || it.category.contains(CATEGORY_CRYPTO_EN) 
            }
            filterForex -> items.filter { 
                it.category.contains(CATEGORY_FOREX_KO) || it.category.contains(CATEGORY_FOREX_EN) 
            }
            filterEconomy -> items.filter { 
                it.category.contains(CATEGORY_ECONOMY_KO) || it.category.contains(CATEGORY_ECONOMY_EN) 
            }
            filterRealEstate -> items.filter { 
                it.category.contains(CATEGORY_REAL_ESTATE_KO) || it.category.contains(CATEGORY_REAL_ESTATE_EN) 
            }
            else -> items
        }

        if (sourceFilter.isNotBlank()) {
            filteredItems = filteredItems.filter { it.source == sourceFilter }
        }

        return filteredItems
    }

    /**
     * Update the active category filter
     * 
     * @param item New filter to apply
     */
    fun updateFilter(item: FabButtonItem) {
        if (item == filterAll) {
            sourceFilter.update { "" }
        }
        categoryFilter.update { item }
        Timber.d("Finance filter updated: ${item.label}")
    }

    /**
     * Update the source filter
     * 
     * @param source Source name to filter by
     */
    fun updateSource(source: String) {
        sourceFilter.update { source }
        Timber.d("Finance source filter updated: $source")
    }

    /**
     * Trigger a refresh of RSS data
     */
    fun refresh() {
        refreshTrigger.update { it + 1 }
        Timber.d("Finance RSS refresh triggered")
    }

    /**
     * Clear all cached RSS data
     */
    fun clearCache() {
        financeRssRepository.clearCache()
        Timber.d("Finance cache cleared from ViewModel")
    }

    /**
     * Invalidate cache for a specific RSS source
     * 
     * @param sourceUrl URL of the source to invalidate
     */
    fun invalidateCacheForSource(sourceUrl: String) {
        financeRssRepository.invalidateCacheForSource(sourceUrl)
        Timber.d("Finance cache invalidated for source: $sourceUrl")
    }

    /**
     * Get cache information for debugging purposes
     * 
     * @return Map of cache entries with timestamps
     */
    fun getCacheInfo(): Map<String, Long> {
        return financeRssRepository.getCacheInfo()
    }
}
