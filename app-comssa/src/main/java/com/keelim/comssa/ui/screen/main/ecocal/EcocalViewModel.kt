package com.keelim.comssa.ui.screen.main.ecocal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.fab.FabButtonItem
import com.keelim.data.source.firebase.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

import androidx.compose.runtime.Stable
@Stable
@HiltViewModel
class EcocalViewModel @Inject constructor(
    firebaseRepository: FirebaseRepository,
) : ViewModel() {
    private val ref = firebaseRepository
        .getRef("ecocal")
        .mapLatest { result ->
            Timber.d("result $result")
            result.getOrThrow()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val filter = MutableStateFlow<FabButtonItem>(All())
    val items = combine(ref, filter) { data, filter ->
        Timber.d("data $data filter $filter")
        val sample = when (filter) {
            is All -> data
            is High -> data.filter { it.priority == "상" }
            is Medium -> data.filter { it.priority == "중" }
            is Low -> data.filter { it.priority == "하" }
            else -> emptyList()
        }
        sample
    }.catch { throwable ->
        Timber.e(throwable)
        emitAll(emptyFlow())
    }
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.loading())

    fun updateFilter(item: FabButtonItem) {
        filter.update { item }
        Timber.d("lab filter ${filter.value}")
    }
}
