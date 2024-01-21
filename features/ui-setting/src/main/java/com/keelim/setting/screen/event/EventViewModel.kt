package com.keelim.setting.screen.event

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@JvmInline
value class EventResult(
    val eventId: Int,
) {
    companion object {
        fun eventResultInitial(savedStateHandle: SavedStateHandle): EventResult {
            return EventResult(
                eventId = checkNotNull(savedStateHandle["eventId"]),
            )
        }
    }
}

@Stable
@HiltViewModel
class EventViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _data = MutableStateFlow(EventResult.eventResultInitial(savedStateHandle))
    val data: StateFlow<EventResult> = _data.asStateFlow()
}
