package com.keelim.setting.screen.settings

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keelim.data.json.JsonParser
import com.keelim.data.json.decode
import com.keelim.data.repository.FirebaseRepository
import com.keelim.shared.data.UserState
import com.keelim.shared.data.UserStateStore
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable
import timber.log.Timber
import jakarta.inject.Inject

@Serializable
data class FamilyService(
    val title: String,
    val imageUrl: String = "",
    val actionUrl: String,
)

@Stable
sealed interface SettingsUiState {
    data object Initialized : SettingsUiState

    data class Success(
        val userState: UserState,
        val fcmToken: String,
        val familyServices: List<FamilyService> = emptyList(),
    ) : SettingsUiState
}

@Stable
@HiltViewModel
class SettingsViewModel @Inject constructor(
    userStateStore: Lazy<UserStateStore>,
    firebaseRepository: Lazy<FirebaseRepository>,
    jsonParser: JsonParser,
) : ViewModel() {
    private val userState = userStateStore
        .get()
        .userState

    private val firebaseInfo = firebaseRepository
        .get()
        .getFCMToken()

    private val familyServices: Flow<List<FamilyService>> = flow {
        val remoteConfigString = firebaseRepository.get().getValue("family_services")
        Timber.d("[SettingsViewModel] Remote config 'family_services' fetched, length: ${remoteConfigString.length}")
        val services: List<FamilyService> = try {
            if (remoteConfigString.isNotEmpty()) {
                val list = jsonParser.decode<List<FamilyService>>(
                    remoteConfigString
                )
                Timber.d("[SettingsViewModel] Parsed ${list.size} family services")
                list
            } else {
                Timber.d("[SettingsViewModel] Remote config string is empty")
                emptyList()
            }
        } catch (e: Exception) {
            Timber.e(e, "[SettingsViewModel] Failed to parse family services")
            emptyList()
        }
        emit(services + FamilyService("Coming Soon", actionUrl = ""))
    }

    val uiState: StateFlow<SettingsUiState> =
        combine(userState, firebaseInfo, familyServices) { userState, fcmToken, services ->
            SettingsUiState.Success(
                userState = userState,
                fcmToken = fcmToken.getOrNull().orEmpty(),
                familyServices = services,
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), SettingsUiState.Initialized)
}
