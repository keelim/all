package com.keelim.testing.platform

import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.privacy.PrivacySettings
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.flow.first

class PlatformFakesTest : FunSpec({
    test("notification fake replaces duplicate ids and records cancellation") {
        val scheduler = FakeNotificationScheduler()
        val first = request("same", "first")
        val replacement = request("same", "replacement")

        scheduler.schedule(first)
        scheduler.schedule(replacement)
        scheduler.scheduledRequests shouldBe listOf(replacement)

        scheduler.cancel("same")
        scheduler.cancelledIds shouldBe listOf("same")
        scheduler.scheduledRequests shouldBe emptyList()
    }

    test("privacy settings and export fakes expose deterministic state") {
        val settingsRepository = FakePrivacySettingsRepository()
        val settings = PrivacySettings(true, true, true, Duration.ofMinutes(3))
        settingsRepository.update(settings)
        settingsRepository.observe().first() shouldBe settings

        val writer = FakeExportFileWriter()
        writer.write(
            com.keelim.common.platform.export.ExportDocument(
                "records.json",
                "application/json",
                "{}".encodeToByteArray(),
            ),
        )
        writer.documents.single().fileName shouldBe "records.json"
    }
})

private fun request(id: String, title: String) = NotificationRequest(
    id = id,
    scheduledAt = Instant.EPOCH,
    channel = NotificationChannelKey("test", "Test", "Test notifications"),
    content = NotificationContent(title, "body"),
)
