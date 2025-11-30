package com.keelim.setting.screen.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.setting.di.DeviceInfo
import com.keelim.setting.di.DeviceInfoSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DeviceInfoViewModel @Inject constructor(
    deviceInfoSource: DeviceInfoSource,
) : ViewModel() {
    val uiState: StateFlow<DeviceInfo> = deviceInfoSource.getDeviceInfo()
        .map { it ?: DeviceInfo.empty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DeviceInfo.empty(),
        )
}
