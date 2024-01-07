package com.keelim.mygrade.ui.screen.task

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.local.LocalTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@Stable
@HiltViewModel
class TaskViewModel @Inject constructor(
    val taskRepository: DefaultTaskRepository,
) : ViewModel() {

    val state: StateFlow<SealedUiState<List<TaskElement>>> = taskRepository
        .observeAll()
        .mapLatest { it.toTaskListSections().toTaskElement() }
        .asSealedUiState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SealedUiState.Loading)
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
