package com.keelim.comssa.ui.screen.main.ecocal

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.core.data.source.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@Stable
@HiltViewModel
class EcocalViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
) : ViewModel() {
    private val ref = firebaseRepository
        .getRef("ecocal")
        .mapLatest { result ->
            result.getOrThrow()
                .map { item -> item.toModel() }
        }
        .catch { throwable ->
            Timber.e(throwable)
            emitAll(emptyFlow())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val filter = MutableStateFlow<FabButtonItem>(All())
    val items = combine(ref, filter) { data, filter ->
        Timber.d("data $data filter $filter")
        when (filter) {
            is All -> data
            is High -> data.filter { it.priority == EcocalPriority.HIGH }
            is Medium -> data.filter { it.priority == EcocalPriority.MEDIUM }
            is Low -> data.filter { it.priority == EcocalPriority.LOW }
            else -> emptyList()
        }
    }.map {
        it.groupBy { it.date }
    }.flowOn(Dispatchers.Default)
        .asSealedUiState()
        .catch { throwable ->
            Timber.e(throwable)
            emitAll(emptyFlow())
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.loading())

    fun updateFilter(item: FabButtonItem) {
        filter.update { item }
        Timber.d("lab filter ${filter.value}")
    }
}
