package com.keelim.mygrade.ui.screen.task

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.repository.DefaultTaskRepository
import com.keelim.model.LocalTask
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
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@Stable
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TaskViewModel @Inject constructor(
    val taskRepository: DefaultTaskRepository,
) : ViewModel() {

    private val retryRequests = MutableStateFlow(0)

    val state: StateFlow<SealedUiState<List<TaskElement>>> = retryRequests
        .flatMapLatest { attempt ->
            flow {
                if (attempt > 0) {
                    taskRepository.refresh()
                }
                emitAll(taskRepository.observeAll())
            }
                .mapLatest { it.toTaskListSections().toTaskElement() }
                .asSealedUiState(emptyToLoading = false)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SealedUiState.Loading)

    fun retry() {
        retryRequests.update { it + 1 }
    }

    fun addLocalTask() {
        viewModelScope.launch {
            taskRepository.create()
        }
    }

    fun editTask(task: LocalTask) {
        viewModelScope.launch {
            taskRepository.upsert(task)
        }
    }

    fun deleteTask(task: LocalTask) {
        taskRepository.delete(task)
    }

    fun clear() {
        taskRepository.clear()
    }
}
