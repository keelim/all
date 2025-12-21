package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.NandaRepository
import com.keelim.model.NandaDiagnosis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import jakarta.inject.Inject

@Stable
@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    nandaRepository: NandaRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val screenState: StateFlow<DiagnosisScreenState> = combine(
        nandaRepository.nandaDiagnosis,
        _query,
    ) { items, query ->
        val categoryItems = filterByCategory(items, savedStateHandle.get<String>("num"))
        if (categoryItems.isEmpty()) {
            DiagnosisScreenState.Empty
        } else {
            val filtered = if (query.isBlank()) {
                categoryItems
            } else {
                categoryItems.filter { it.reason.contains(query, ignoreCase = true) }
            }

            if (filtered.isEmpty()) {
                DiagnosisScreenState.Empty
            } else {
                DiagnosisScreenState.Success(filtered.map { DiagnosisItem(it.reason, "") })
            }
        }
    }.catch {
        emit(DiagnosisScreenState.Error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), DiagnosisScreenState.Loading)

    fun search(newQuery: String) {
        _query.value = newQuery
    }

    private fun filterByCategory(items: List<NandaDiagnosis>, num: String?): List<NandaDiagnosis> {
        val (startPoint, endPoint) = when (num) {
            "1" -> 0 to 11
            "2" -> 12 to 22
            "3" -> 23 to 32
            "4" -> 33 to 51
            "5" -> 52 to 86
            "6" -> 87 to 97
            "7" -> 98 to 108
            "8" -> 109 to 123
            "9" -> 124 to 129
            "10" -> 130 to 167
            "11" -> 168 to 178
            "12" -> 179 to 205
            "13" -> 204 to 223
            else -> return emptyList()
        }

        return if (items.size > endPoint) {
            items.subList(startPoint, endPoint + 1)
        } else {
            emptyList()
        }
    }
}

@Stable
data class DiagnosisItem(val diagnosis: String, val description: String)

@Stable
sealed interface DiagnosisScreenState {
    data object Loading : DiagnosisScreenState
    data object Empty : DiagnosisScreenState
    data object Error : DiagnosisScreenState
    data class Success(val items: List<DiagnosisItem>) : DiagnosisScreenState
}
