package com.keelim.mygrade.ui.screen.timer

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@Stable
enum class RunningState {
    STOPPED, STARTED
}

internal fun formatTime(isLeadingZeroNeeded: Boolean = false, value: Int): String {
    return if (isLeadingZeroNeeded) {
        String.format("%02d", value)
    } else {
        String.format("%2d", value)
    }
}

internal val HOUR_LIST = (0..12).toList()
internal val MINUTE_LIST = (0..60).toList()
internal val SECOND_LIST = (0..60).toList()

data class TimerUiState(
    val isUnsetDialog: Boolean = false,
)

@HiltViewModel
class TimerViewModel @Inject constructor() : ViewModel() {
    private var countTimeJob: Job? = null
    private var _isRunning by mutableStateOf(RunningState.STOPPED)

    private val _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState: StateFlow<TimerUiState> = _timerUiState.asStateFlow()

    val isRunning
        get() = _isRunning

    private var _hour by mutableIntStateOf(0)
    var hour: Int
        get() = _hour
        set(value) {
            _hour = value
        }

    private var _minute by mutableIntStateOf(0)
    var minute: Int
        get() = _minute
        set(value) {
            _minute = value
        }

    private var _second by mutableIntStateOf(0)
    var second: Int
        get() = _second
        set(value) {
            _second = value
        }

    val leftTime = mutableIntStateOf(0)

    fun getTotalTimeInSeconds(): Int {
        return (hour * 3600 + minute * 60 + second)
    }

    fun addTime(currTime: Long): String {
        val setTime = getTotalTimeInSeconds() * 1000
        val addedTime = setTime + currTime
        return SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date(addedTime))
    }

    fun start() {
        leftTime.intValue = getTotalTimeInSeconds()
        if (leftTime.intValue <= 0) {
            _timerUiState.update { old ->
                old.copy(
                    isUnsetDialog = true,
                )
            }
            return
        }
        _isRunning = RunningState.STARTED
        countTimeJob = tick(
            leftTime.intValue,
        ).onEach {
            leftTime.intValue = it
        }.launchIn(viewModelScope)
    }

    fun stop() {
        countTimeJob?.cancel()
        _isRunning = RunningState.STOPPED
    }

    fun clearDialog() {
        _timerUiState.update { old ->
            old.copy(
                isUnsetDialog = false,
            )
        }
    }

    private fun tick(
        leftTime: Int,
        duration: Long = 1000L,
    ): Flow<Int> = flow {
        var i = leftTime
        while (i > 0) {
            delay(duration)
            emit(--i)
        }
    }
}
