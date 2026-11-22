package com.keelim.setting.screen.lab

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.PromptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

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
            try {
                val content = promptRepository
                    .getContent(prompt = prompt)
                    .getOrThrow()
                _uiState.value = LabUiState.Success(content)
            } catch (e: CancellationException) {
                throw e
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                _uiState.value = LabUiState.Error(throwable.localizedMessage ?: "")
            }
        }
    }
}
