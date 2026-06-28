package com.keelim.mygrade.ui.screen.timer

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.extensions.formatUiTime
import com.keelim.common.extensions.toUiTwoDigits
import com.keelim.data.repository.HistoryRepository
import com.keelim.data.repository.StudyAnalyticsRepository
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
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import jakarta.inject.Inject

@Stable
enum class RunningState {
    STOPPED, STARTED
}

internal val HOUR_LIST = (0..12).toList()
internal val MINUTE_LIST = (0..60).toList()
internal val SECOND_LIST = (0..60).toList()

data class TimerUiState(
    val isUnsetDialog: Boolean = false,
    val runningState: RunningState = RunningState.STOPPED,
    val hour: Int = 0,
    val minute: Int = 0,
    val second: Int = 0,
    val leftTime: Int = 0,
)

@Stable
@HiltViewModel
class TimerViewModel @Inject constructor(
    private val studyAnalyticsRepository: StudyAnalyticsRepository,
    private val historyRepository: HistoryRepository,
) : ViewModel() {
    private var countTimeJob: Job? = null

    private val _timerUiState = MutableStateFlow(TimerUiState())
    val timerUiState: StateFlow<TimerUiState> = _timerUiState.asStateFlow()

    val isRunning
        get() = _timerUiState.value.runningState

    var hour: Int
        get() = _timerUiState.value.hour
        set(value) {
            _timerUiState.update { old ->
                old.copy(hour = value)
            }
        }

    var minute: Int
        get() = _timerUiState.value.minute
        set(value) {
            _timerUiState.update { old ->
                old.copy(minute = value)
            }
        }

    var second: Int
        get() = _timerUiState.value.second
        set(value) {
            _timerUiState.update { old ->
                old.copy(second = value)
            }
        }

    val leftTime: Int
        get() = _timerUiState.value.leftTime

    private var initialTotalSeconds = 0

    fun getTotalTimeInSeconds(): Int {
        return (hour * 3600 + minute * 60 + second)
    }

    fun addTime(currTime: Long): String {
        val setTime = getTotalTimeInSeconds() * 1000
        val addedTime = setTime + currTime
        val localDateTime = Instant.fromEpochMilliseconds(addedTime)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val hour = when (localDateTime.hour) {
            0 -> 12
            in 13..23 -> localDateTime.hour - 12
            else -> localDateTime.hour
        }
        val period = if (localDateTime.hour < 12) "AM" else "PM"
        return "${formatUiTime(hour = hour, minute = localDateTime.minute)}:${localDateTime.second.toUiTwoDigits()} $period"
    }

    fun start() {
        val initialLeftTime = getTotalTimeInSeconds()
        initialTotalSeconds = initialLeftTime
        if (initialLeftTime <= 0) {
            _timerUiState.update { old ->
                old.copy(
                    isUnsetDialog = true,
                )
            }
            return
        }
        countTimeJob?.cancel()
        _timerUiState.update { old ->
            old.copy(
                runningState = RunningState.STARTED,
                leftTime = initialLeftTime,
                isUnsetDialog = false,
            )
        }
        countTimeJob = tick(
            initialLeftTime,
        ).onEach {
            _timerUiState.update { old ->
                old.copy(leftTime = it)
            }
        }.launchIn(viewModelScope)
    }

    fun stop() {
        countTimeJob?.cancel()
        _timerUiState.update { old ->
            old.copy(runningState = RunningState.STOPPED)
        }
    }

    fun onTimerComplete() {
        if (initialTotalSeconds > 0) {
            val completedHours = initialTotalSeconds / 3600
            val completedMinutes = (initialTotalSeconds % 3600) / 60
            val completedSeconds = initialTotalSeconds % 60

            viewModelScope.launch {
                studyAnalyticsRepository.recordSession(
                    subject = "Default",
                    durationSeconds = initialTotalSeconds,
                )
                historyRepository.createTimerHistory(
                    hours = completedHours,
                    minutes = completedMinutes,
                    seconds = completedSeconds,
                )
            }
        }
    }

    fun clear() {
        countTimeJob?.cancel()
        initialTotalSeconds = 0
        _timerUiState.update { old ->
            old.copy(
                runningState = RunningState.STOPPED,
                hour = 0,
                minute = 0,
                second = 0,
                leftTime = 0,
                isUnsetDialog = false,
            )
        }
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
