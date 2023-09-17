package com.keelim.mygrade.ui.screen.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MainScreenState(
    val subjectError: Boolean = false,
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

    private val _subject = MutableStateFlow("")
    val subject: StateFlow<String> = _subject.asStateFlow()

    private val _origin = MutableStateFlow("")
    val origin: StateFlow<String> = _origin.asStateFlow()

    private val _average = MutableStateFlow("")
    val average: StateFlow<String> = _average.asStateFlow()

    private val _number = MutableStateFlow("")
    val number: StateFlow<String> = _number.asStateFlow()

    private val _student = MutableStateFlow("")
    val student: StateFlow<String> = _student.asStateFlow()

    fun <T: EditType> updateEditType(editType: T){
        editType.value.trim()
            .also { value ->
                when(editType) {
                    is EditType.Average -> _average.tryEmit(value)
                    is EditType.Number -> _number.tryEmit(value)
                    is EditType.Origin -> _origin.tryEmit(value)
                    is EditType.Student -> _student.tryEmit(value)
                    is EditType.Subject -> _subject.tryEmit(value)
                }
            }
        updateStateEmpty()
    }

    // TODO: 이때 데이터 베이스에 저장을 할 것
    fun submit() {
        val subject = runCatching {
            _subject.value
        }.getOrNull()

        if(subject == null) {
            _mainScreenState.update { old ->
                old.copy(
                    originError = true,
                )
            }
        }
        val origin = runCatching {
            _origin.value.toFloat()
        }.getOrNull()

        if (origin == null) {
            _mainScreenState.update { old ->
                old.copy(
                    originError = true,
                )
            }
        }
        val average = runCatching {
            _average.value.toFloat()
        }.getOrNull()

        if (average == null) {
            _mainScreenState.update { old ->
                old.copy(
                    averageError = true,
                )
            }
        }

        val number = runCatching {
            _number.value.toFloat()
        }.getOrNull()

        if (number == null) {
            _mainScreenState.update { old ->
                old.copy(
                    numberError = true,
                )
            }
        }
        val student = runCatching {
            _student.value.toInt()
        }.getOrNull()
        if (student == null) {
            _mainScreenState.update { old ->
                old.copy(
                    studentError = true,
                )
            }
        }
        subject ?: return
        origin ?: return
        average ?: return
        number ?: return
        student ?: return
        _state.tryEmit(
            MainState.Success(
                flag = true,
                subject = subject,
                value = Zvalue(((origin - average) / number).toDouble()).getNormalProbability(),
                student = student,
            ),
        )
    }

    fun moveToUnInitialized() {
        _state.tryEmit(
            MainState.UnInitialized,
        )
    }

    fun updateStateEmpty() {
        _mainScreenState.tryEmit(
            MainScreenState.empty(),
        )
    }
}

sealed interface EditType {
    val value: String
    data class Subject(override val value: String): EditType
    data class Origin(override val value: String): EditType
    data class Average(override val value: String): EditType
    data class Number(override val value: String): EditType
    data class Student(override val value: String): EditType
}
