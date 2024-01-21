package com.keelim.nandadiagnosis.ui.screen.category

import androidx.compose.runtime.Stable
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
@Stable
@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {
    val state: StateFlow<CategoryState> = flow {
        emit(CategoryState.Empty)
        emit(
            CategoryState.Success(
                persistentListOf(
                    "건강증진",
                    "영양",
                    "배설/교환",
                    "활동/휴식",
                    "지각/인식",
                    "자아지각",
                    "역할관계",
                    "성",
                    "대처/스트레스 내성",
                    "삶의 원칙",
                    "안전/보호",
                    "안위",
                    "성장/발달",
                ),
            ),
        )
    }.catch {
        emit(CategoryState.Error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), CategoryState.Empty)
}

sealed interface CategoryState {
    data object Loading : CategoryState
    data object Empty : CategoryState
    data object Error : CategoryState
    data class Success(val items: PersistentList<String>) : CategoryState
}
