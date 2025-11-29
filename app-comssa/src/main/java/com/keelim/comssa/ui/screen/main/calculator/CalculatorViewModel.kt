package com.keelim.comssa.ui.screen.main.calculator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.calculator.CalculatorHistoryRepository
import com.keelim.model.CalculatorHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repository: CalculatorHistoryRepository,
) : ViewModel() {

    val history = repository.getAllHistory()
        .map { list ->
            list.map { it.toUiModel() }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList(),
        )

    fun addHistory(type: CalculatorTypeUi, input: Map<String, String>, result: Map<String, String>) {
        viewModelScope.launch {
            repository.addHistory(
                CalculatorHistory(
                    id = System.currentTimeMillis().toString(),
                    type = type.domainType,
                    input = input,
                    result = result,
                    timestamp = System.currentTimeMillis(),
                ),
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }
}

private fun CalculatorHistory.toUiModel(): CalculatorHistoryUi =
    CalculatorHistoryUi(
        id = id,
        type = CalculatorTypeUi.values().firstOrNull { it.domainType == type }
            ?: CalculatorTypeUi.COMPOUND_INTEREST,
        input = input,
        result = result,
        timestamp = timestamp,
    )
