package com.keelim.mygrade.ui.screen.task.add

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.di.IoDispatcher
import com.keelim.data.source.DefaultTaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
sealed interface TaskAddState {
    data object Empty: TaskAddState
    data object Loading: TaskAddState
    data object Success: TaskAddState
}
@HiltViewModel
class TaskAddViewModel @Inject constructor(
    val defaultTaskRepository: DefaultTaskRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow<TaskAddState>(TaskAddState.Empty)
    val uiState : StateFlow<TaskAddState> = _uiState.asStateFlow()


    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            _uiState.tryEmit(TaskAddState.Loading)
            withContext(dispatcher) {
                val id = defaultTaskRepository.create(
                    title = title, description = description
                )
            }
            _uiState.tryEmit(TaskAddState.Success)
        }
    }
}
