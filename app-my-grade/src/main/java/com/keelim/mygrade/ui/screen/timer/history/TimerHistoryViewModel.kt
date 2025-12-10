package com.keelim.mygrade.ui.screen.timer.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.HistoryRepository
import com.keelim.model.TimerHistoryModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TimerHistoryUiState(
    val histories: List<TimerHistoryModel> = emptyList(),
    val isLoading: Boolean = true,
)

@Stable
@HiltViewModel
class TimerHistoryViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
) : ViewModel() {

    val uiState: StateFlow<TimerHistoryUiState> = historyRepository.observeTimerHistories()
        .map { histories ->
            TimerHistoryUiState(
                histories = histories,
                isLoading = false,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerHistoryUiState(),
        )

    fun deleteHistory(historyId: Int) {
        viewModelScope.launch {
            historyRepository.deleteTimerHistory(historyId)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            historyRepository.deleteAllTimerHistories()
        }
    }

    fun updateDescription(historyId: Int, description: String) {
        viewModelScope.launch {
            historyRepository.updateTimerHistoryDescription(historyId, description)
        }
    }
}
