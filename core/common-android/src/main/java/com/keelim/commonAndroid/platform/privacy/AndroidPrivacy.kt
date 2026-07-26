package com.keelim.commonAndroid.platform.privacy

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.FragmentActivity
import com.keelim.common.platform.privacy.AuthenticationError
import com.keelim.common.platform.privacy.AuthenticationResult
import com.keelim.common.platform.privacy.PrivacyAuthenticator
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacySettingsRepository
import java.time.Duration
import java.util.concurrent.Executor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private val Context.platformPrivacyDataStore by preferencesDataStore("platform_privacy_settings")

class DataStorePrivacySettingsRepository(
    context: Context,
    private val defaults: PrivacySettings,
) : PrivacySettingsRepository {
    private val dataStore = context.platformPrivacyDataStore

    override fun observe(): Flow<PrivacySettings> = dataStore.data.map { preferences ->
        PrivacySettings(
            appLockEnabled = preferences[APP_LOCK] ?: defaults.appLockEnabled,
            obscureRecentApps = preferences[OBSCURE_RECENTS] ?: defaults.obscureRecentApps,
            blockScreenshots = preferences[BLOCK_SCREENSHOTS] ?: defaults.blockScreenshots,
            autoLockTimeout = Duration.ofMillis(
                preferences[AUTO_LOCK_TIMEOUT] ?: defaults.autoLockTimeout.toMillis(),
            ),
        )
    }

    override suspend fun update(settings: PrivacySettings) {
        dataStore.edit { preferences ->
            preferences[APP_LOCK] = settings.appLockEnabled
            preferences[OBSCURE_RECENTS] = settings.obscureRecentApps
            preferences[BLOCK_SCREENSHOTS] = settings.blockScreenshots
            preferences[AUTO_LOCK_TIMEOUT] = settings.autoLockTimeout.toMillis()
        }
    }

    private companion object {
        val APP_LOCK = booleanPreferencesKey("app_lock_enabled")
        val OBSCURE_RECENTS = booleanPreferencesKey("obscure_recent_apps")
        val BLOCK_SCREENSHOTS = booleanPreferencesKey("block_screenshots")
        val AUTO_LOCK_TIMEOUT = longPreferencesKey("auto_lock_timeout_ms")
    }
}

class BiometricPrivacyAuthenticator(
    private val activity: FragmentActivity,
    private val title: String,
    private val subtitle: String,
    private val executor: Executor = activity.mainExecutor,
) : PrivacyAuthenticator {
    override suspend fun authenticate(): AuthenticationResult {
        val authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG
        if (BiometricManager.from(activity).canAuthenticate(authenticators) !=
            BiometricManager.BIOMETRIC_SUCCESS
        ) {
            return AuthenticationResult.NotAvailable
        }

        return suspendCancellableCoroutine { continuation ->
            val prompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult,
                    ) {
                        if (continuation.isActive) continuation.resume(AuthenticationResult.Success)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        if (!continuation.isActive) return
                        val result = when (errorCode) {
                            BiometricPrompt.ERROR_CANCELED,
                            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
                            BiometricPrompt.ERROR_USER_CANCELED,
                            -> AuthenticationResult.Cancelled
                            BiometricPrompt.ERROR_HW_NOT_PRESENT,
                            BiometricPrompt.ERROR_HW_UNAVAILABLE,
                            BiometricPrompt.ERROR_NO_BIOMETRICS,
                            -> AuthenticationResult.NotAvailable
                            else -> AuthenticationResult.Error(AuthenticationError.UNKNOWN)
                        }
                        continuation.resume(result)
                    }

                    override fun onAuthenticationFailed() = Unit
                },
            )
            continuation.invokeOnCancellation { prompt.cancelAuthentication() }
            prompt.authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setAllowedAuthenticators(authenticators)
                    .setNegativeButtonText(activity.getString(android.R.string.cancel))
                    .build(),
            )
        }
    }
}
