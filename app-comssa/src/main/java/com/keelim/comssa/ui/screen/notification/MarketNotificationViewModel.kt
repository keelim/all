package com.keelim.comssa.ui.screen.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.model.MarketSchedule
import com.keelim.data.repository.MarketNotificationRepository
import com.keelim.comssa.notification.MarketNotificationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import jakarta.inject.Inject

@HiltViewModel
class MarketNotificationViewModel @Inject constructor(
    private val repository: MarketNotificationRepository,
    private val notificationManager: MarketNotificationManager
) : ViewModel() {

    val schedules: StateFlow<List<MarketSchedule>> = repository.getSchedules()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _showTimePicker = MutableStateFlow(false)
    val showTimePicker: StateFlow<Boolean> = _showTimePicker

    fun toggleSchedule(schedule: MarketSchedule) {
        viewModelScope.launch {
            val updated = schedule.copy(isEnabled = !schedule.isEnabled)
            repository.updateSchedule(updated)

            if (updated.isEnabled) {
                notificationManager.scheduleNotification(updated)
            } else {
                notificationManager.cancelNotification(updated)
            }
        }
    }

    fun addCustomSchedule(name: String, hour: Int, minute: Int) {
        viewModelScope.launch {
            val schedule = MarketSchedule(
                id = UUID.randomUUID().toString(),
                name = name,
                hour = hour,
                minute = minute,
                isEnabled = true,
                isDefault = false
            )
            repository.addSchedule(schedule)
            notificationManager.scheduleNotification(schedule)
        }
    }

    fun removeSchedule(schedule: MarketSchedule) {
        viewModelScope.launch {
            notificationManager.cancelNotification(schedule)
            repository.removeSchedule(schedule.id)
        }
    }

    fun showTimePicker() {
        _showTimePicker.value = true
    }

    fun hideTimePicker() {
        _showTimePicker.value = false
    }

    fun rescheduleAllEnabled() {
        viewModelScope.launch {
            schedules.value.filter { it.isEnabled }.forEach { schedule ->
                notificationManager.scheduleNotification(schedule)
            }
        }
    }
}
