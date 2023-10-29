package com.keelim.setting.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.setting.di.DeviceInfo
import com.keelim.setting.di.DeviceInfoSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    deviceInfoSource: DeviceInfoSource
): ViewModel() {
    val deviceInfo: StateFlow<DeviceInfo?> = deviceInfoSource
        .getDeviceInfo()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
}
