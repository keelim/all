package com.keelim.nandadiagnosis.ui.screen.diagnosis

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import androidx.compose.runtime.Stable
@Stable
@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var nav: Int = 0
    private val _data = MutableStateFlow<List<String>>(emptyList())
    val screenState: StateFlow<DiagnosisScreenState> = _data.mapLatest { items ->
        if (items.isEmpty()) {
            DiagnosisScreenState.Empty
        } else {
            DiagnosisScreenState.Success(items.map { DiagnosisItem(it, "") })
        }
    }.catch {
        emit(DiagnosisScreenState.Error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), DiagnosisScreenState.Loading)

    fun initArray(targets: Array<String>) {
        _data.tryEmit(targets.toList())
        val (startPoint, endPoint) = when (savedStateHandle.get<String>("num")) {
            "1" -> {
                nav = 0
                nav to 11
            }

            "2" -> {
                nav = 12
                nav to 22
            }

            "3" -> {
                nav = 23
                nav to 32
            }

            "4" -> {
                nav = 33
                nav to 51
            }

            "5" -> {
                nav = 52
                nav to 86
            }

            "6" -> {
                nav = 87
                nav to 97
            }

            "7" -> {
                nav = 98
                nav to 108
            }

            "8" -> {
                nav = 109
                nav to 123
            }

            "9" -> {
                nav = 124
                nav to 129
            }

            "10" -> {
                nav = 130
                nav to 167
            }

            "11" -> {
                nav = 168
                nav to 178
            }

            "12" -> {
                nav = 179
                nav to 205
            }

            "13" -> {
                nav = 204
                nav to 223
            }

            else -> return
        }
        customAdd(startPoint, endPoint)
    }

    private fun customAdd(startPoint: Int, finalPoint: Int) {
        _data.value
            .takeIf { it.isNotEmpty() }
            ?.also {
                val items = it.subList(startPoint, finalPoint)
                _data.tryEmit(items)
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
