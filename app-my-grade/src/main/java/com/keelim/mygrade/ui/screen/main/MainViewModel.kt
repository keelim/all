package com.keelim.mygrade.ui.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class MainScreenState(
    val originError: Boolean = false,
    val averageError: Boolean = false,
    val numberError: Boolean = false,
    val studentError: Boolean = false,
) {
    companion object {
        fun empty(): MainScreenState {
            return MainScreenState()
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private val _state: MutableStateFlow<MainState> = MutableStateFlow(MainState.UnInitialized)
    val state: StateFlow<MainState> = _state.asStateFlow()

    private val _mainScreenState = MutableStateFlow(MainScreenState.empty())
    val mainScreenState: StateFlow<MainScreenState> = _mainScreenState.asStateFlow()


    private val _origin = MutableStateFlow<String>("")
    val origin: StateFlow<String> = _origin.asStateFlow()

    private val _average = MutableStateFlow<String>("")
    val average: StateFlow<String> = _average.asStateFlow()

    private val _number = MutableStateFlow<String>("")
    val number: StateFlow<String> = _number.asStateFlow()

    private val _student = MutableStateFlow<String>("")
    val student: StateFlow<String> = _student.asStateFlow()

    fun updateOrigin(origin: String) {
        origin.trim()
            .also(_origin::tryEmit)

    }

    fun updateAverage(average: String) {
        average.trim()
            .also(_average::tryEmit)
        updateStateEmpty()
    }

    fun updateNumber(number: String) {
        number.trim()
            .also(_number::tryEmit)
        updateStateEmpty()
    }

    fun updateStudent(student: String) {
        student.trim()
            .also(_student::tryEmit)
        updateStateEmpty()
    }

    // TODO: 이때 데이터 베이스에 저장을 할 것
    fun submit() {
        val origin = runCatching {
            _origin.value.toFloat()
        }.getOrNull()
        if (origin == null) {
            _mainScreenState.update { old ->
                old.copy(
                    originError = true
                )
            }
        }
        val average = runCatching {
            _average.value.toFloat()
        }.getOrNull()

        if (average == null) {
            _mainScreenState.update { old ->
                old.copy(
                    averageError = true
                )
            }
        }

        val number = runCatching {
            _number.value.toFloat()
        }.getOrNull()

        if (number == null) {
            _mainScreenState.update { old ->
                old.copy(
                    numberError = true
                )
            }
        }
        val student = runCatching {
            _student.value.toInt()
        }.getOrNull()
        if (student == null) {
            _mainScreenState.update { old ->
                old.copy(
                    studentError = true
                )
            }
        }
        origin ?: return
        average ?: return
        number ?: return
        student ?: return
        _state.tryEmit(
            MainState.Success(
                true,
                Zvalue(((origin - average) / number).toDouble()).getNormalProbability(),
                student,
            )
        )
    }

    fun moveToUnInitialized() {
        _state.tryEmit(
            MainState.UnInitialized
        )
    }

    fun updateStateEmpty() {
        _mainScreenState.tryEmit(
            MainScreenState.empty()
        )
    }
}
