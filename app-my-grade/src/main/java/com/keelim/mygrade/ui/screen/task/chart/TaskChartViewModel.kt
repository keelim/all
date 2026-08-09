package com.keelim.mygrade.ui.screen.task.chart

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.canvas.chart.PieChartEntry
import com.keelim.composeutil.util.randomColor
import com.keelim.data.repository.DefaultTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import jakarta.inject.Inject

@Stable
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskChartViewModel
@Inject
constructor(
    taskRepository: DefaultTaskRepository,
) : ViewModel() {

    private val retryRequests = MutableStateFlow(0)

    val state: StateFlow<SealedUiState<List<PieChartEntry>>> = retryRequests
        .flatMapLatest { attempt ->
            flow {
                if (attempt > 0) {
                    taskRepository.refresh()
                }
                emitAll(taskRepository.observeAll())
            }
                .mapLatest {
                    it.map { task ->
                        PieChartEntry(
                            name = task.title,
                            color = randomColor(),
                            percentage = (1f / it.size),
                        )
                    }
                }
                .asSealedUiState(emptyToLoading = false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SealedUiState.Loading)

    fun retry() {
        retryRequests.update { it + 1 }
    }
}
