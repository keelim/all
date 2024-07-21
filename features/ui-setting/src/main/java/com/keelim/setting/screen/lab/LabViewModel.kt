package com.keelim.setting.screen.lab

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
@Stable
@HiltViewModel
class LabViewModel @Inject constructor(
    val promptRepository: PromptRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LabUiState> =
        MutableStateFlow(LabUiState.Initial)
    val uiState: StateFlow<LabUiState> =
        _uiState.asStateFlow()

    fun queuePrompt(inputText: String) {
        _uiState.value = LabUiState.Loading

        val prompt = "Summarize the following text for me: $inputText"

        viewModelScope.launch {
            runCatching {
                promptRepository
                    .getContent(prompt = prompt)
                    .getOrThrow()
            }.onSuccess { content ->
                _uiState.value = LabUiState.Success(content)
            }.onFailure { throwable ->
                Timber.e(throwable)
                _uiState.value = LabUiState.Error(throwable.localizedMessage ?: "")
            }
        }
    }
}
