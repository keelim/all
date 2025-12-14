package com.keelim.arducon.ui.screen.json

import androidx.lifecycle.ViewModel
import com.keelim.data.json.JsonParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class JsonFormatterViewModel @Inject constructor(
    private val jsonParser: JsonParser,
) : ViewModel() {
    private val _uiState = MutableStateFlow(JsonFormatterUiState())
    val uiState: StateFlow<JsonFormatterUiState> = _uiState.asStateFlow()

    fun updateInputJson(input: String) {
        _uiState.update { it.copy(inputJson = input) }
    }

    fun formatJson() {
        val input = _uiState.value.inputJson
        try {
            val formatted = jsonParser.formatJson(input)
            _uiState.update { it.copy(formattedJson = formatted, errorMessage = null) }
        } catch (e: Exception) {
            _uiState.update { it.copy(formattedJson = "", errorMessage = e.message) }
        }
    }

    fun clear() {
        _uiState.update { JsonFormatterUiState() }
    }
}

data class JsonFormatterUiState(
    val inputJson: String = "",
    val formattedJson: String = "",
    val errorMessage: String? = null,
)
