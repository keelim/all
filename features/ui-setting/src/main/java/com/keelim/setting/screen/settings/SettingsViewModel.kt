package com.keelim.setting.screen.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.setting.di.DeviceInfo
import com.keelim.setting.di.DeviceInfoSource
import com.keelim.shared.data.UserState
import com.keelim.shared.data.UserStateStore
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface SettingsUiState {
    data object Initialized : SettingsUiState

    data class Success(
        val userState: UserState,
        val deviceInfo: DeviceInfo,
    ) : SettingsUiState
}

@Stable
@HiltViewModel
class SettingsViewModel @Inject constructor(
    deviceInfoSource: Lazy<DeviceInfoSource>,
    userStateStore: Lazy<UserStateStore>,
) : ViewModel() {
    private val userState = userStateStore
        .get()
        .userState

    private val deviceInfo = deviceInfoSource
        .get()
        .getDeviceInfo()

    val uiState: StateFlow<SettingsUiState> = combine(userState, deviceInfo) { userState, deviceInfo ->
        SettingsUiState.Success(
            userState = userState,
            deviceInfo = deviceInfo ?: DeviceInfo.empty(),
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), SettingsUiState.Initialized)
}
