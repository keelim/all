package com.keelim.mygrade.ui.screen.center.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.UnInitialized)
    val state: StateFlow<MainState> = _state.asStateFlow()

    // TODO: 이때 데이터 베이스에 저장을 할 것
    fun submit(
        origin: Float,
        average: Float,
        number: Float,
        student: Int,
        flag: Boolean = false
    ) = viewModelScope.launch {
        _state.update { MainState.Loading }
        if (flag.not()) return@launch
        runCatching {
            _state.update { MainState.Initialized }
            true
        }.onSuccess { trigger ->
            _state.update {
                MainState.Success(
                    trigger,
                    Zvalue(((origin - average) / number).toDouble()).getNormalProbability(),
                    student,
                )
            }
        }.onFailure {
            _state.emit(MainState.Error("실패"))
        }
    }

    fun moveToUnInitialized() {
        _state.update { MainState.UnInitialized }
    }
}
