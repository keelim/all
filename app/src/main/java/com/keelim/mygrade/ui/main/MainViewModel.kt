package com.keelim.mygrade.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.math3.distribution.NormalDistribution

@HiltViewModel
class MainViewModel @Inject constructor(

) : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.UnInitialized)
    val state: StateFlow<MainState> = _state

    fun submit(origin: Float, average: Float, number: Float, student: Int, flag: Boolean = false) =
        viewModelScope.launch {
            _state.emit(MainState.Loading)
            if (flag.not()) return@launch

            runCatching {
                true
            }.onSuccess {
                _state.emit(
                    MainState.Success(
                        it,
                        getNormalProbabilityAtZ(((origin - average) / number).toDouble()),
                    )
                )
            }.onFailure {
                _state.emit(MainState.Error("실패"))
            }
        }

    private fun getNormalProbabilityAtZ(z: Double): Int {
        val normal = NormalDistribution()
        return if (z > 0) {
            100 - ((normal.probability(0.0, z) + 0.5) * 100).toInt()
        } else {
            ((normal.probability(0.0, -z) + 0.5) * 100).toInt()
        }
    }
}