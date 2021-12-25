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
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val ioRepository: IoRepository
): ViewModel() {
    private var _state:MutableStateFlow<HistoryState> = MutableStateFlow(HistoryState.UnInitialized)
    val state:StateFlow<HistoryState> get() = _state
    private val data:MutableStateFlow<List<History>> = MutableStateFlow(emptyList())
    init{
        observeHistory()
    }

    private fun observeHistory() {
        ioRepository.all
            .onStart {
                _state.emit(HistoryState.Loading)
            }.onEach {
                data.value = it
                _state.emit(HistoryState.Success(it))
            }.catch {
                it.printStackTrace()
                _state.emit(HistoryState.Error(it.message!!))
            }.launchIn(viewModelScope)
    }

    private fun historyPaging(){

    }
}