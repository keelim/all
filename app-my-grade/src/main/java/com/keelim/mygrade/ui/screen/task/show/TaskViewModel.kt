package com.keelim.mygrade.ui.screen.task.show

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.local.LocalTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.Clock
import timber.log.Timber
import javax.inject.Inject

@Stable
sealed interface TaskUiState {
    data object Loading : TaskUiState
    data object Empty : TaskUiState
    data object Error : TaskUiState

    data class Success(val tasks: PersistentList<LocalTask>) : TaskUiState
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    defaultTaskRepository: DefaultTaskRepository,
) : ViewModel() {
    private val trigger = MutableStateFlow(Clock.System.now())
    val taskUiState: StateFlow<TaskUiState> = combine(defaultTaskRepository.observeAll(), trigger) { tasks, _ ->
        if (tasks.isEmpty()) {
            TaskUiState.Empty
        } else {
            TaskUiState.Success(tasks.toPersistentList())
        }
    }.catch { throwable ->
        Timber.e(throwable)
        TaskUiState.Error
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TaskUiState.Loading)

    fun refresh() {
        trigger.tryEmit(Clock.System.now())
    }
}
