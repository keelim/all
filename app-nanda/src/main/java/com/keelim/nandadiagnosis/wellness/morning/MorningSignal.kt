package com.keelim.nandadiagnosis.wellness.morning

import android.content.SharedPreferences

data class MorningSignalSettings(
    val enabled: Boolean = false,
    val hour: Int = DEFAULT_HOUR,
    val minute: Int = DEFAULT_MINUTE,
)

data class MorningSignalNotificationContent(
    val title: String,
    val body: String,
    val channelName: String,
    val channelDescription: String,
)

enum class MorningSignalMessage {
    NOTIFICATION_PERMISSION_DENIED,
    SCHEDULING_FAILED,
}

data class MorningSignalUiState(
    val settings: MorningSignalSettings = MorningSignalSettings(),
    val message: MorningSignalMessage? = null,
)

interface MorningSignalSettingsStore {
    fun load(): MorningSignalSettings
    fun save(settings: MorningSignalSettings)
}

class SharedPreferencesMorningSignalSettingsStore(
    private val preferences: SharedPreferences,
) : MorningSignalSettingsStore {
    override fun load(): MorningSignalSettings = MorningSignalSettings(
        enabled = preferences.getBoolean(KEY_ENABLED, false),
        hour = preferences.getInt(KEY_HOUR, DEFAULT_HOUR).coerceIn(0, 23),
        minute = preferences.getInt(KEY_MINUTE, DEFAULT_MINUTE).coerceIn(0, 59),
    )

    override fun save(settings: MorningSignalSettings) {
        preferences.edit()
            .putBoolean(KEY_ENABLED, settings.enabled)
            .putInt(KEY_HOUR, settings.hour.coerceIn(0, 23))
            .putInt(KEY_MINUTE, settings.minute.coerceIn(0, 59))
            .apply()
    }

    private companion object {
        const val KEY_ENABLED = "enabled"
        const val KEY_HOUR = "hour"
        const val KEY_MINUTE = "minute"
    }
}

private const val DEFAULT_HOUR = 7
private const val DEFAULT_MINUTE = 0
