package com.keelim.setting.screen.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.repository.FirebaseRepository
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

@Stable
sealed interface SettingsUiState {
    data object Initialized : SettingsUiState

    data class Success(
        val userState: UserState,
        val deviceInfo: DeviceInfo,
        val fcmToken: String,
    ) : SettingsUiState
}

@Stable
@HiltViewModel
class SettingsViewModel @Inject constructor(
    deviceInfoSource: Lazy<DeviceInfoSource>,
    userStateStore: Lazy<UserStateStore>,
    firebaseRepository: Lazy<FirebaseRepository>,
) : ViewModel() {
    private val userState = userStateStore
        .get()
        .userState

    private val deviceInfo = deviceInfoSource
        .get()
        .getDeviceInfo()

    private val firebaseInfo = firebaseRepository
        .get()
        .getFCMToken()

    val uiState: StateFlow<SettingsUiState> =
        combine(userState, deviceInfo, firebaseInfo) { userState, deviceInfo, fcmToken ->
            SettingsUiState.Success(
                userState = userState,
                deviceInfo = deviceInfo ?: DeviceInfo.empty(),
                fcmToken = fcmToken.getOrNull().orEmpty(),
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SettingsUiState.Initialized)
}
