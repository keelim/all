package com.keelim.mygrade.ui.screen.task.show

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.local.LocalTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@Stable
sealed interface TaskScreenState {
    data object Loading : TaskScreenState
    data object Empty : TaskScreenState
    data object Error : TaskScreenState

    data class Success(val tasks: PersistentList<LocalTask>) : TaskScreenState
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    val defaultTaskRepository: DefaultTaskRepository,
) : ViewModel() {
    val taskScreenState: StateFlow<TaskScreenState> = defaultTaskRepository.observeAll()
        .mapLatest { tasks ->
            if (tasks.isEmpty()) {
                TaskScreenState.Empty
            } else {
                TaskScreenState.Success(tasks.toPersistentList())
            }
        }.catch { throwable ->
            Timber.e(throwable)
            TaskScreenState.Error
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), TaskScreenState.Loading)

    fun clearTask() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                defaultTaskRepository.clear()
            }
        }
    }
}
