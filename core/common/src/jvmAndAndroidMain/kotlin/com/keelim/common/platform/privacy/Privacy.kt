package com.keelim.common.platform.privacy

import com.keelim.common.platform.time.TimeProvider
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PrivacySettings(
    val appLockEnabled: Boolean,
    val obscureRecentApps: Boolean,
    val blockScreenshots: Boolean,
    val autoLockTimeout: Duration,
) {
    init {
        require(!autoLockTimeout.isNegative) { "autoLockTimeout must not be negative" }
    }

    companion object {
        val Disabled = PrivacySettings(
            appLockEnabled = false,
            obscureRecentApps = false,
            blockScreenshots = false,
            autoLockTimeout = Duration.ZERO,
        )
    }
}

data class PrivacyState(
    val settings: PrivacySettings = PrivacySettings.Disabled,
    val isUnlocked: Boolean = true,
)

interface PrivacySettingsRepository {
    fun observe(): Flow<PrivacySettings>
    suspend fun update(settings: PrivacySettings)
}

interface PrivacyAuthenticator {
    suspend fun authenticate(): AuthenticationResult
}

sealed interface AuthenticationResult {
    data object Success : AuthenticationResult
    data object Cancelled : AuthenticationResult
    data object NotAvailable : AuthenticationResult
    data class Error(val reason: AuthenticationError) : AuthenticationResult
}

enum class AuthenticationError {
    HARDWARE_UNAVAILABLE,
    AUTHENTICATION_FAILED,
    UNKNOWN,
}

interface PrivacyController {
    val isUnlocked: StateFlow<Boolean>
    fun onBackgrounded(settings: PrivacySettings)
    fun onForegrounded(settings: PrivacySettings)
    fun onAuthenticationResult(result: AuthenticationResult)
}

class DefaultPrivacyController(
    private val timeProvider: TimeProvider,
    initiallyUnlocked: Boolean = true,
) : PrivacyController {
    private val mutableUnlocked = MutableStateFlow(initiallyUnlocked)
    private var backgroundedAt: Instant? = null

    override val isUnlocked: StateFlow<Boolean> = mutableUnlocked.asStateFlow()

    override fun onBackgrounded(settings: PrivacySettings) {
        backgroundedAt = timeProvider.now()
        if (settings.appLockEnabled && settings.autoLockTimeout.isZero) {
            mutableUnlocked.value = false
        }
    }

    override fun onForegrounded(settings: PrivacySettings) {
        if (!settings.appLockEnabled) {
            mutableUnlocked.value = true
            backgroundedAt = null
            return
        }
        val backgroundTime = backgroundedAt ?: return
        if (Duration.between(backgroundTime, timeProvider.now()) >= settings.autoLockTimeout) {
            mutableUnlocked.value = false
        }
        backgroundedAt = null
    }

    override fun onAuthenticationResult(result: AuthenticationResult) {
        if (result == AuthenticationResult.Success) mutableUnlocked.value = true
    }
}
