package com.keelim.mygrade.ui.screen.task

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.data.repository.DefaultTaskRepository
import com.keelim.model.LocalTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for task management screen following MVVM pattern
 * Handles task CRUD operations and state management with proper coroutines usage
 */
@Stable
@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: DefaultTaskRepository,
) : ViewModel() {

    /**
     * StateFlow representing the current state of tasks
     * Automatically updates when repository data changes
     */
    val state: StateFlow<SealedUiState<List<TaskElement>>> = taskRepository
        .observeAll()
        .mapLatest { it.toTaskListSections().toTaskElement() }
        .asSealedUiState()
        .stateIn(
            scope = viewModelScope, 
            started = SharingStarted.WhileSubscribed(5_000L), 
            initialValue = SealedUiState.Loading
        )
    
    /**
     * Add a new local task
     * Uses viewModelScope to ensure proper lifecycle handling
     */
    fun addLocalTask() {
        viewModelScope.launch {
            taskRepository.create()
        }
    }

    /**
     * Update or insert a task
     * 
     * @param task The task to update or insert
     */
    fun editTask(task: LocalTask) {
        viewModelScope.launch {
            taskRepository.upsert(task)
        }
    }

    /**
     * Delete a specific task
     * 
     * @param task The task to delete
     */
    fun deleteTask(task: LocalTask) {
        viewModelScope.launch {
            taskRepository.delete(task)
        }
    }

    /**
     * Clear all tasks
     * Uses viewModelScope to ensure proper lifecycle handling
     */
    fun clear() {
        viewModelScope.launch {
            taskRepository.clear()
        }
    }
}
