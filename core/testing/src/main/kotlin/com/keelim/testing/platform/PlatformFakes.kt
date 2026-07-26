package com.keelim.testing.platform

import com.keelim.common.platform.analytics.AnalyticsEvent
import com.keelim.common.platform.analytics.AnalyticsTracker
import com.keelim.common.platform.export.ExportDocument
import com.keelim.common.platform.export.ExportFileWriter
import com.keelim.common.platform.export.ExportedFile
import com.keelim.common.platform.featureflag.FeatureFlag
import com.keelim.common.platform.featureflag.FeatureFlagProvider
import com.keelim.common.platform.featureflag.LocalOverrideFeatureFlagProvider
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import com.keelim.common.platform.notification.NotificationScheduler
import com.keelim.common.platform.observability.AppLogger
import com.keelim.common.platform.observability.CrashReporter
import com.keelim.common.platform.privacy.AuthenticationResult
import com.keelim.common.platform.privacy.PrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacySettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

typealias FakeTimeProvider = com.keelim.common.platform.time.FakeTimeProvider

class FakeAnalyticsTracker : AnalyticsTracker {
    private val mutableEvents = mutableListOf<AnalyticsEvent>()
    val events: List<AnalyticsEvent> get() = mutableEvents.toList()

    override fun track(event: AnalyticsEvent) {
        mutableEvents += event
    }
}

class FakeFeatureFlagProvider : FeatureFlagProvider {
    private val delegate = LocalOverrideFeatureFlagProvider()

    override fun observe(flag: FeatureFlag): Flow<Boolean> = delegate.observe(flag)
    override suspend fun isEnabled(flag: FeatureFlag): Boolean = delegate.isEnabled(flag)
    fun set(flag: FeatureFlag, enabled: Boolean?) = delegate.setOverride(flag, enabled)
}

class FakePrivacySettingsRepository(
    initialSettings: PrivacySettings = PrivacySettings.Disabled,
) : PrivacySettingsRepository {
    private val settings = MutableStateFlow(initialSettings)

    override fun observe(): Flow<PrivacySettings> = settings
    override suspend fun update(settings: PrivacySettings) {
        this.settings.value = settings
    }
}

class FakePrivacyController(
    initiallyUnlocked: Boolean = true,
) : PrivacyController {
    private val mutableUnlocked = MutableStateFlow(initiallyUnlocked)
    override val isUnlocked: StateFlow<Boolean> = mutableUnlocked.asStateFlow()
    var backgroundCount: Int = 0
        private set

    override fun onBackgrounded(settings: PrivacySettings) {
        backgroundCount++
        if (settings.appLockEnabled) mutableUnlocked.value = false
    }

    override fun onForegrounded(settings: PrivacySettings) {
        if (!settings.appLockEnabled) mutableUnlocked.value = true
    }

    override fun onAuthenticationResult(result: AuthenticationResult) {
        mutableUnlocked.value = result == AuthenticationResult.Success
    }

    fun setUnlocked(unlocked: Boolean) {
        mutableUnlocked.value = unlocked
    }
}

class FakeNotificationScheduler : NotificationScheduler {
    private val requestsById = linkedMapOf<String, NotificationRequest>()
    val scheduledRequests: List<NotificationRequest> get() = requestsById.values.toList()
    val cancelledIds = mutableListOf<String>()
    val cancelledTags = mutableListOf<String>()
    var scheduleResult: NotificationScheduleResult = NotificationScheduleResult.Scheduled

    override suspend fun schedule(request: NotificationRequest): NotificationScheduleResult {
        if (scheduleResult == NotificationScheduleResult.Scheduled) requestsById[request.id] = request
        return scheduleResult
    }

    override suspend fun cancel(id: String) {
        cancelledIds += id
        requestsById.remove(id)
    }

    override suspend fun cancelByTag(tag: String) {
        cancelledTags += tag
        requestsById.entries.removeAll { it.value.tag == tag }
    }
}

class FakeAppLogger : AppLogger {
    val entries = mutableListOf<String>()

    override fun debug(message: String, attributes: Map<String, String>) {
        entries += "DEBUG $message"
    }

    override fun info(message: String, attributes: Map<String, String>) {
        entries += "INFO $message"
    }

    override fun warning(message: String, throwable: Throwable?) {
        entries += "WARN $message"
    }

    override fun error(message: String, throwable: Throwable?) {
        entries += "ERROR $message"
    }
}

class FakeCrashReporter : CrashReporter {
    data class Record(val throwable: Throwable, val attributes: Map<String, String>)
    val records = mutableListOf<Record>()

    override fun recordException(throwable: Throwable, attributes: Map<String, String>) {
        records += Record(throwable, attributes.toMap())
    }
}

class FakeExportFileWriter : ExportFileWriter {
    val documents = mutableListOf<ExportDocument>()
    var result: ExportedFile = ExportedFile("fake://export", "application/octet-stream")

    override suspend fun write(document: ExportDocument): ExportedFile {
        documents += document.copy(content = document.content.copyOf())
        return result.copy(mimeType = document.mimeType)
    }
}
