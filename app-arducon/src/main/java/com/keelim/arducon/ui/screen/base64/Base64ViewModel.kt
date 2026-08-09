package com.keelim.arducon.ui.screen.base64

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.core.data.repository.Base64Repository
import com.keelim.shared.data.database.model.Base64History
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import jakarta.inject.Inject

@HiltViewModel
class Base64ViewModel @Inject constructor(
    private val repository: Base64Repository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(Base64UiState())
    val uiState: StateFlow<Base64UiState> = _uiState.asStateFlow()

    val history: StateFlow<List<Base64History>> = repository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateInputText(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun updateSelectedIndex(index: Int) {
        _uiState.update { it.copy(selectedIndex = index) }
    }

    fun processBase64() {
        val input = _uiState.value.inputText
        val isEncode = _uiState.value.selectedIndex == 0
        try {
            val output = if (isEncode) {
                Base64.encodeToString(input.toByteArray(), Base64.DEFAULT).trim()
            } else {
                String(Base64.decode(input, Base64.DEFAULT))
            }
            _uiState.update { it.copy(outputText = output, errorMessage = null) }
            saveHistory(input, isEncode)
        } catch (e: Exception) {
            _uiState.update { it.copy(outputText = "", errorMessage = e.message) }
        }
    }

    private fun saveHistory(text: String, isEncoded: Boolean) {
        viewModelScope.launch {
            repository.insertHistory(text, isEncoded)
        }
    }

    fun deleteHistory(history: Base64History) {
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

    fun clear() {
        _uiState.update { Base64UiState(selectedIndex = it.selectedIndex) }
    }
}

data class Base64UiState(
    val inputText: String = "",
    val outputText: String = "",
    val selectedIndex: Int = 0, // 0: Encode, 1: Decode
    val errorMessage: String? = null,
)
