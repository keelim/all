package com.keelim.common.platform

import com.keelim.common.platform.appshell.AppShellPresentation
import com.keelim.common.platform.appshell.AppShellState
import com.keelim.common.platform.appshell.resolveAppShellPresentation
import com.keelim.common.platform.export.ExportDocument
import com.keelim.common.platform.export.ExportDocumentPolicy
import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationPolicy
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.privacy.DefaultPrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacyState
import com.keelim.common.platform.time.FakeTimeProvider
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class PlatformCapabilitiesTest : FunSpec({
    test("fake time advances in its configured timezone") {
        val time = FakeTimeProvider(
            initialInstant = Instant.parse("2026-07-25T23:30:00Z"),
            zone = ZoneId.of("Asia/Seoul"),
        )
        time.today().toString() shouldBe "2026-07-26"
        time.advanceBy(Duration.ofHours(24))
        time.now() shouldBe Instant.parse("2026-07-26T23:30:00Z")
    }

    test("privacy locks after the configured background timeout and stays open when disabled") {
        val time = FakeTimeProvider(Instant.parse("2026-07-26T00:00:00Z"), ZoneId.of("UTC"))
        val controller = DefaultPrivacyController(time)
        val enabled = PrivacySettings(true, true, true, Duration.ofMinutes(5))

        controller.onBackgrounded(enabled)
        time.advanceBy(Duration.ofMinutes(5))
        controller.onForegrounded(enabled)
        controller.isUnlocked.value shouldBe false

        controller.onForegrounded(PrivacySettings.Disabled)
        controller.isUnlocked.value shouldBe true
    }

    test("app shell prioritizes recents obscuring and app lock") {
        val settings = PrivacySettings(true, true, true, Duration.ZERO)
        resolveAppShellPresentation(
            appState = AppShellState(),
            privacyState = PrivacyState(settings, isUnlocked = false),
            isForeground = false,
        ) shouldBe AppShellPresentation.OBSCURED
        resolveAppShellPresentation(
            appState = AppShellState(),
            privacyState = PrivacyState(settings, isUnlocked = false),
            isForeground = true,
        ) shouldBe AppShellPresentation.LOCKED
    }

    test("notification validation and neutral public content are deterministic") {
        val request = NotificationRequest(
            id = "",
            scheduledAt = Instant.EPOCH,
            channel = NotificationChannelKey("plan", "Plan", "Plan reminders"),
            content = NotificationContent("Private", "Private detail"),
        )
        NotificationPolicy.violations(request) shouldContain "notification id must not be blank"
        NotificationPolicy.publicContent(request.content).publicBody shouldBe
            "앱을 열어 예정된 활동을 확인하세요."
    }

    test("export policy rejects traversal") {
        ExportDocumentPolicy.violations(
            ExportDocument("../records.json", "application/json", byteArrayOf(1)),
        ) shouldContain "invalid export file name"
        ExportDocumentPolicy.violations(
            ExportDocument("records.json", "application/json", byteArrayOf()),
        ) shouldContain "export content must not be empty"
    }
})
