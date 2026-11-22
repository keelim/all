package com.keelim.nandadiagnosis.wellness.morning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationPolicy
import com.keelim.common.platform.notification.NotificationRepeat
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import com.keelim.common.platform.notification.NotificationScheduler
import com.keelim.common.platform.time.TimeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import java.time.Instant
import java.time.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MorningSignalViewModel @Inject constructor(
    private val settingsStore: MorningSignalSettingsStore,
    private val notificationScheduler: NotificationScheduler,
    private val timeProvider: TimeProvider,
    private val notificationContent: MorningSignalNotificationContent,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        MorningSignalUiState(settings = settingsStore.load()),
    )
    val uiState = _uiState.asStateFlow()

    init {
        val settings = _uiState.value.settings
        if (settings.enabled) {
            viewModelScope.launch { schedule(settings, disableOnFailure = true) }
        }
    }

    fun setEnabled(enabled: Boolean) {
        if (!enabled) {
            viewModelScope.launch {
                notificationScheduler.cancel(NOTIFICATION_ID)
                val settings = _uiState.value.settings.copy(enabled = false)
                settingsStore.save(settings)
                _uiState.value = MorningSignalUiState(settings = settings)
            }
            return
        }

        viewModelScope.launch {
            schedule(_uiState.value.settings.copy(enabled = true), disableOnFailure = true)
        }
    }

    fun setTime(hour: Int, minute: Int) {
        val current = _uiState.value.settings
        val settings = current.copy(
            hour = hour.coerceIn(0, 23),
            minute = minute.coerceIn(0, 59),
        )
        if (!current.enabled) {
            settingsStore.save(settings)
            _uiState.value = MorningSignalUiState(settings = settings)
            return
        }

        viewModelScope.launch {
            schedule(settings, disableOnFailure = true)
        }
    }

    fun onPermissionDenied() {
        _uiState.update { it.copy(message = MorningSignalMessage.NOTIFICATION_PERMISSION_DENIED) }
    }

    private suspend fun schedule(
        settings: MorningSignalSettings,
        disableOnFailure: Boolean,
    ) {
        when (runCatching { notificationScheduler.schedule(settings.toRequest()) }
            .getOrElse { NotificationScheduleResult.Invalid(listOf("notification scheduling failed")) }) {
            NotificationScheduleResult.Scheduled -> {
                settingsStore.save(settings)
                _uiState.value = MorningSignalUiState(settings = settings)
            }

            NotificationScheduleResult.PermissionDenied -> {
                val nextSettings = if (disableOnFailure) {
                    settings.copy(enabled = false)
                } else {
                    _uiState.value.settings
                }
                settingsStore.save(nextSettings)
                _uiState.value = MorningSignalUiState(
                    settings = nextSettings,
                    message = MorningSignalMessage.NOTIFICATION_PERMISSION_DENIED,
                )
            }

            is NotificationScheduleResult.Invalid -> {
                _uiState.update { it.copy(message = MorningSignalMessage.SCHEDULING_FAILED) }
            }
        }
    }

    private fun MorningSignalSettings.toRequest(): NotificationRequest = NotificationRequest(
        id = NOTIFICATION_ID,
        scheduledAt = nextOccurrence(hour, minute),
        channel = NotificationChannelKey(
            id = CHANNEL_ID,
            name = notificationContent.channelName,
            description = notificationContent.channelDescription,
        ),
        content = NotificationContent(
            title = notificationContent.title,
            body = notificationContent.body,
            publicTitle = NotificationPolicy.PUBLIC_TITLE,
            publicBody = NotificationPolicy.PUBLIC_BODY,
        ),
        tag = NOTIFICATION_TAG,
        repeat = NotificationRepeat.DAILY,
    )

    private fun nextOccurrence(hour: Int, minute: Int): Instant {
        val now = timeProvider.now()
        val zone = timeProvider.zoneId()
        val todayAt = now.atZone(zone)
            .toLocalDate()
            .atTime(LocalTime.of(hour, minute))
            .atZone(zone)
        return if (todayAt.toInstant().isAfter(now)) {
            todayAt.toInstant()
        } else {
            todayAt.plusDays(1).toInstant()
        }
    }

    private companion object {
        const val NOTIFICATION_ID = "morning-signal"
        const val NOTIFICATION_TAG = "morning-signal"
        const val CHANNEL_ID = "morning_signal"
    }
}
