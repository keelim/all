package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.data.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import jakarta.inject.Inject

@Stable
@HiltViewModel
class EcocalViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository,
) : ViewModel() {
    private val priorityFilter = MutableStateFlow<FabButtonItem>(Clear())
    private val countryFilter = MutableStateFlow<String>("")
    private val retrySignal = MutableStateFlow(0)

    val items = retrySignal
        .flatMapLatest {
            val ref = firebaseRepository
                .getRef("ecocal")
                .mapLatest { result ->
                    result.getOrThrow()
                        .map { item -> item.toModel() }
                }

            combine(ref, priorityFilter, countryFilter) { data, filter, country ->
                Timber.d("data $data filter $filter")
                when (filter) {
                    is Clear -> data
                    is High -> data.filter { it.priority == EcocalPriority.HIGH }
                    is Medium -> data.filter { it.priority == EcocalPriority.MEDIUM }
                    is Low -> data.filter { it.priority == EcocalPriority.LOW }
                    else -> emptyList()
                }.let {
                    if (country.isBlank()) {
                        it
                    } else {
                        it.filter { item ->
                            item.country == country
                        }
                    }
                }
            }.map { filteredItems ->
                filteredItems.groupBy { it.date }
            }
                .flowOn(Dispatchers.Default)
                .asSealedUiState()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SealedUiState.loading())

    fun updateFilter(item: FabButtonItem) {
        if (item is Clear) {
            countryFilter.update { "" }
        }
        priorityFilter.update { item }
        Timber.d("lab filter ${priorityFilter.value}")
    }

    fun updateCountry(country: String) {
        countryFilter.update { country }
        Timber.d("lab filter ${countryFilter.value}")
    }

    fun retry() {
        retrySignal.update { it + 1 }
    }
}
