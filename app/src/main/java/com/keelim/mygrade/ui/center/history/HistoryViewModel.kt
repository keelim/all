package com.keelim.mygrade.ui.center.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.db.entity.History
import com.keelim.data.repository.IoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val ioRepository: IoRepository
) : ViewModel() {
    private var _state: MutableStateFlow<HistoryState> =
        MutableStateFlow(HistoryState.UnInitialized)
    val state: StateFlow<HistoryState> get() = _state

    init {
        init()
    }

    fun init() = viewModelScope.launch {
        _state.emit(HistoryState.UnInitialized)
        runCatching {
            _state.emit(HistoryState.Loading)
            ioRepository.getAllHistories()
        }.onSuccess {
            _state.emit(HistoryState.Success(it))
        }.onFailure {
            it.printStackTrace()
            _state.emit(HistoryState.Error("에러가 발생하였습니다."))
        }
    }
}