package com.keelim.mygrade.ui.screen.task.show

import androidx.lifecycle.viewModelScope
import com.keelim.commonAndroid.model.SealedUiState
import com.keelim.commonAndroid.model.asSealedUiState
import com.keelim.commonAndroid.core.KeelimViewModel
import com.keelim.commonAndroid.core.UserAction
import com.keelim.data.source.DefaultTaskRepository
import com.keelim.data.source.local.LocalTask
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


sealed interface TaskUserAction: UserAction {
    data object ClearTask: TaskUserAction
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val defaultTaskRepository: DefaultTaskRepository,
) : KeelimViewModel<SealedUiState<List<LocalTask>>, TaskUserAction>(
    startWith = SealedUiState.Loading
) {
    init {
        viewModelScope.launch {
            defaultTaskRepository.observeAll()
                .asSealedUiState()
                .collect {
                    emitState(it)
                }
        }
    }

    override suspend fun processUserAction(userAction: TaskUserAction) {
        when (userAction) {
            TaskUserAction.ClearTask -> {
                defaultTaskRepository.clear()
            }
        }
    }
}
