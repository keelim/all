package com.keelim.testing.platform

import com.keelim.common.platform.export.ExportDocument
import com.keelim.common.platform.export.ExportFileWriter
import com.keelim.common.platform.export.ExportedFile
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.notification.NotificationScheduleResult
import com.keelim.common.platform.notification.NotificationScheduler
import com.keelim.common.platform.privacy.AuthenticationResult
import com.keelim.common.platform.privacy.PrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacySettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

typealias FakeTimeProvider = com.keelim.common.platform.time.FakeTimeProvider

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

class FakeExportFileWriter : ExportFileWriter {
    val documents = mutableListOf<ExportDocument>()
    var result: ExportedFile = ExportedFile("fake://export", "application/octet-stream")

    override suspend fun write(document: ExportDocument): ExportedFile {
        documents += document.copy(content = document.content.copyOf())
        return result.copy(mimeType = document.mimeType)
    }
}
