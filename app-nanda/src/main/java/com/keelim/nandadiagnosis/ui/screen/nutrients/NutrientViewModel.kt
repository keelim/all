package com.keelim.nandadiagnosis.ui.screen.nutrients

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NutrientViewModel @Inject constructor() : ViewModel() {
    val state: StateFlow<NutrientState> = flow {
        emit(NutrientState.Loading)
        emit(
            NutrientState.Success(
                persistentListOf(
                    "비타민 A" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_A",
                    "비타민 B1" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B2" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B3" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B5" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B6" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B7" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B9" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 B12" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_B_%EB%B3%B5%ED%95%A9%EC%B2%B4",
                    "비타민 C" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_C",
                    "비타민 D" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_D",
                    "비타민 E" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_E",
                    "비타민 K" to "https://ko.wikipedia.org/wiki/%EB%B9%84%ED%83%80%EB%AF%BC_K",
                ),
            ),
        )
    }.catch {
        emit(NutrientState.Error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NutrientState.Empty)
}

sealed interface NutrientState {
    data object Loading : NutrientState
    data object Empty : NutrientState
    data object Error : NutrientState
    data class Success(val items: PersistentList<Pair<String, String>>) : NutrientState
}
