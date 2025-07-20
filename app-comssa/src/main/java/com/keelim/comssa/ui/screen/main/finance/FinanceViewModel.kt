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

@Stable
@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val financeRssRepository: FinanceRssRepository,
) : ViewModel() {

    private val filterAll = FilterAll()
    private val filterStock = FilterStock()
    private val filterCrypto = FilterCrypto()
    private val filterForex = FilterForex()
    private val filterEconomy = FilterEconomy()
    private val filterRealEstate = FilterRealEstate()

    private val categoryFilter = MutableStateFlow<FabButtonItem>(filterAll)
    private val sourceFilter = MutableStateFlow<String>("")
    private val refreshTrigger = MutableStateFlow(0)

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
            Timber.e(throwable)
            emitAll(emptyFlow())
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            SealedUiState.loading()
        )

    val filterButtons = listOf(
        filterAll,
        filterStock,
        filterCrypto,
        filterForex,
        filterEconomy,
        filterRealEstate,
    )

    private fun filterItems(
        items: List<FinanceRssItem>,
        categoryFilter: FabButtonItem,
        sourceFilter: String
    ): List<FinanceRssItem> {
        var filteredItems = when (categoryFilter) {
            filterAll -> items
            filterStock -> items.filter { it.category.contains("주식") || it.category.contains("Stock") }
            filterCrypto -> items.filter { it.category.contains("암호화폐") || it.category.contains("Crypto") }
            filterForex -> items.filter { it.category.contains("외환") || it.category.contains("Forex") }
            filterEconomy -> items.filter { it.category.contains("경제") || it.category.contains("Economy") }
            filterRealEstate -> items.filter { it.category.contains("부동산") || it.category.contains("Real Estate") }
            else -> items
        }

        if (sourceFilter.isNotBlank()) {
            filteredItems = filteredItems.filter { it.source == sourceFilter }
        }

        return filteredItems
    }

    fun updateFilter(item: FabButtonItem) {
        if (item == filterAll) {
            sourceFilter.update { "" }
        }
        categoryFilter.update { item }
        Timber.d("Finance filter updated: ${item.label}")
    }

    fun updateSource(source: String) {
        sourceFilter.update { source }
        Timber.d("Finance source filter updated: $source")
    }

    fun refresh() {
        refreshTrigger.update { it + 1 }
        Timber.d("Finance RSS refresh triggered")
    }
}
