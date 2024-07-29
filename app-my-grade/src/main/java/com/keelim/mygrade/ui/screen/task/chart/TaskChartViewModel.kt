package com.keelim.mygrade.ui.screen.task.chart

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.composeutil.component.canvas.chart.PieChartEntry
import com.keelim.composeutil.util.randomColor
import com.keelim.core.database.repository.DefaultTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@Stable
@HiltViewModel
class TaskChartViewModel
@Inject
constructor(
    taskRepository: DefaultTaskRepository,
) : ViewModel() {

    val state: StateFlow<SealedUiState<List<PieChartEntry>>> =
        taskRepository
            .observeAll()
            .mapLatest {
                it.map { task ->
                    PieChartEntry(
                        name = task.title,
                        color = randomColor(),
                        percentage = (1f / it.size),
                    )
                }
            }
            .asSealedUiState()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.Loading)
}
