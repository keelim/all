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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Stable
sealed interface TaskAddScreenState {
    data object Empty : TaskAddScreenState

    data object Loading : TaskAddScreenState

    data object Success : TaskAddScreenState
}

@Stable
data class TaskAddUiState(
    val title: String = "",
    val description: String = "",
)

@HiltViewModel
class TaskAddViewModel
@Inject
constructor(
    private val defaultTaskRepository: DefaultTaskRepository,
    @IoDispatcher val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _screenState = MutableStateFlow<TaskAddScreenState>(TaskAddScreenState.Empty)
    val screenState: StateFlow<TaskAddScreenState> = _screenState.asStateFlow()

    private val _uiState = MutableStateFlow(TaskAddUiState())
    val uiState: StateFlow<TaskAddUiState> = _uiState.asStateFlow()

    fun updateTitle(value: String) {
        _uiState.update { old -> old.copy(title = value) }
    }

    fun updateDescription(value: String) {
        _uiState.update { old -> old.copy(description = value) }
    }

    fun addTask() {
        viewModelScope.launch {
            _screenState.tryEmit(TaskAddScreenState.Loading)
            withContext(dispatcher) {
                val currentUiState = _uiState.value
                val id =
                    defaultTaskRepository.create(
                        title =
                        currentUiState.title.ifEmpty {
                            return@withContext
                        },
                        description =
                        currentUiState.description.ifEmpty {
                            return@withContext
                        },
                    )
            }
            _screenState.tryEmit(TaskAddScreenState.Success)
        }
    }
}
