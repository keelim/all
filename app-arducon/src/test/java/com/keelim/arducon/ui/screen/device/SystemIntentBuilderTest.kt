package com.keelim.arducon.ui.screen.device

import android.content.Intent
import android.provider.Settings
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class SystemIntentBuilderTest : FunSpec({
    test("브라우저 인텐트 스펙을 만든다") {
        val spec = DeviceIntentTests.specFor(
            id = DeviceTestId.BrowserIntent,
            packageName = TEST_PACKAGE_NAME,
        )

        spec.action shouldBe Intent.ACTION_VIEW
        spec.dataUri shouldBe "https://developer.android.com/"
        spec.mimeType shouldBe null
        spec.extras shouldBe emptyMap()
    }

    test("공유 인텐트 스펙을 만든다") {
        val spec = DeviceIntentTests.specFor(
            id = DeviceTestId.ShareIntent,
            packageName = TEST_PACKAGE_NAME,
        )

        spec.action shouldBe Intent.ACTION_SEND
        spec.mimeType shouldBe "text/plain"
        spec.extras[Intent.EXTRA_TEXT] shouldBe "Arducon device test"
    }

    test("앱 설정 인텐트 스펙은 패키지 URI를 포함한다") {
        val spec = DeviceIntentTests.specFor(
            id = DeviceTestId.AppSettingsIntent,
            packageName = TEST_PACKAGE_NAME,
        )

        spec.action shouldBe Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        spec.dataUri shouldBe "package:$TEST_PACKAGE_NAME"
    }

    test("알림 설정 인텐트 스펙은 앱 패키지 extra를 포함한다") {
        val spec = DeviceIntentTests.specFor(
            id = DeviceTestId.NotificationSettingsIntent,
            packageName = TEST_PACKAGE_NAME,
        )

        spec.action shouldBe Settings.ACTION_APP_NOTIFICATION_SETTINGS
        spec.extras[Settings.EXTRA_APP_PACKAGE] shouldBe TEST_PACKAGE_NAME
    }

    test("모든 시스템 인텐트 테스트는 스펙을 제공한다") {
        val specs = systemIntentTestIds.map { id ->
            DeviceIntentTests.specFor(
                id = id,
                packageName = TEST_PACKAGE_NAME,
            )
        }

        specs.size shouldBe systemIntentTestIds.size
        specs.all { it.action.isNotBlank() } shouldBe true
    }
})

private const val TEST_PACKAGE_NAME = "com.keelim.arducon"
