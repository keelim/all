package com.keelim.common.platform

import com.keelim.common.platform.analytics.AnalyticsConsentState
import com.keelim.common.platform.analytics.AnalyticsEvent
import com.keelim.common.platform.analytics.ConsentAwareAnalyticsTracker
import com.keelim.common.platform.analytics.DebugAnalyticsTracker
import com.keelim.common.platform.analytics.NoOpAnalyticsTracker
import com.keelim.common.platform.appshell.AppShellPresentation
import com.keelim.common.platform.appshell.AppShellState
import com.keelim.common.platform.appshell.resolveAppShellPresentation
import com.keelim.common.platform.export.ExportDocument
import com.keelim.common.platform.export.ExportDocumentPolicy
import com.keelim.common.platform.featureflag.FeatureFlag
import com.keelim.common.platform.featureflag.LocalOverrideFeatureFlagProvider
import com.keelim.common.platform.notification.NotificationChannelKey
import com.keelim.common.platform.notification.NotificationContent
import com.keelim.common.platform.notification.NotificationPolicy
import com.keelim.common.platform.notification.NotificationRequest
import com.keelim.common.platform.observability.DebugAppLogger
import com.keelim.common.platform.observability.LogRedactionPolicy
import com.keelim.common.platform.privacy.DefaultPrivacyController
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacyState
import com.keelim.common.platform.time.FakeTimeProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import kotlinx.coroutines.flow.first

class PlatformCapabilitiesTest : FunSpec({
    test("analytics blocks sensitive properties and respects disabled consent") {
        val events = mutableListOf<AnalyticsEvent>()
        val debug = DebugAnalyticsTracker(events::add)
        var consent = AnalyticsConsentState.DISABLED
        val tracker = ConsentAwareAnalyticsTracker(debug) { consent }

        tracker.track(AnalyticsEvent("app_opened"))
        events shouldBe emptyList()

        consent = AnalyticsConsentState.ENABLED
        tracker.track(AnalyticsEvent("app_opened"))
        events.map { it.name } shouldBe listOf("app_opened")
        shouldThrow<IllegalArgumentException> {
            debug.track(AnalyticsEvent("check_in_completed", mapOf("health_answer" to "yes")))
        }
        NoOpAnalyticsTracker().track(AnalyticsEvent(""))
    }

    test("feature flags use defaults and emit local override updates") {
        val flag = TestFlag("new_screen", defaultValue = false)
        val provider = LocalOverrideFeatureFlagProvider()
        provider.isEnabled(flag) shouldBe false

        provider.setOverride(flag, true)
        provider.isEnabled(flag) shouldBe true
        provider.observe(flag).first() shouldBe true

        provider.setOverride(flag, null)
        provider.isEnabled(flag) shouldBe false
    }

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

    test("export and logging policies reject traversal and redact sensitive keys") {
        ExportDocumentPolicy.violations(
            ExportDocument("../records.json", "application/json", byteArrayOf(1)),
        ) shouldContain "invalid export file name"
        ExportDocumentPolicy.violations(
            ExportDocument("records.json", "application/json", byteArrayOf()),
        ) shouldContain "export content must not be empty"

        val logs = mutableListOf<String>()
        DebugAppLogger(logs::add).info(
            message = "request",
            attributes = mapOf("authorization" to "secret", "result" to "ok"),
        )
        logs.single().contains(LogRedactionPolicy.REDACTED) shouldBe true
        logs.single().contains("secret") shouldBe false
    }
})

private data class TestFlag(
    override val key: String,
    override val defaultValue: Boolean,
) : FeatureFlag
