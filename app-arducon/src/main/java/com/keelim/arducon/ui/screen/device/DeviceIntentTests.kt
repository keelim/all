package com.keelim.arducon.ui.screen.device

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri

data class SystemIntentSpec(
    val action: String,
    val dataUri: String? = null,
    val mimeType: String? = null,
    val extras: Map<String, String> = emptyMap(),
) {
    fun toIntent(): Intent {
        return Intent(this@SystemIntentSpec.action).apply {
            this@SystemIntentSpec.dataUri?.let { data = it.toUri() }
            this@SystemIntentSpec.mimeType?.let { type = it }
            this@SystemIntentSpec.extras.forEach { (key, value) -> putExtra(key, value) }
        }
    }
}

internal object DeviceIntentTests {
    fun specFor(
        id: DeviceTestId,
        packageName: String,
    ): SystemIntentSpec {
        return when (id) {
            DeviceTestId.BrowserIntent -> SystemIntentSpec(
                action = Intent.ACTION_VIEW,
                dataUri = "https://developer.android.com/",
            )

            DeviceTestId.ShareIntent -> SystemIntentSpec(
                action = Intent.ACTION_SEND,
                mimeType = "text/plain",
                extras = mapOf(Intent.EXTRA_TEXT to "Arducon device test"),
            )

            DeviceTestId.AppSettingsIntent -> SystemIntentSpec(
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                dataUri = "package:$packageName",
            )

            DeviceTestId.NotificationSettingsIntent -> SystemIntentSpec(
                action = Settings.ACTION_APP_NOTIFICATION_SETTINGS,
                extras = mapOf(Settings.EXTRA_APP_PACKAGE to packageName),
            )

            DeviceTestId.DialerIntent -> SystemIntentSpec(
                action = Intent.ACTION_DIAL,
                dataUri = "tel:123456789",
            )

            DeviceTestId.EmailIntent -> SystemIntentSpec(
                action = Intent.ACTION_SENDTO,
                dataUri = "mailto:test@example.com",
            )

            DeviceTestId.MapIntent -> SystemIntentSpec(
                action = Intent.ACTION_VIEW,
                dataUri = "geo:37.5665,126.9780?q=Seoul",
            )

            else -> error("Unsupported system intent test: $id")
        }
    }

    fun run(
        context: Context,
        id: DeviceTestId,
    ): DeviceTestOutcome {
        val intent = specFor(id, context.packageName).toIntent()
        return try {
            context.startActivity(intent)
            DeviceTestOutcome.pass(DeviceTestMessage.IntentLaunched)
        } catch (exception: ActivityNotFoundException) {
            DeviceTestOutcome.fail(DeviceTestMessage.IntentUnavailable)
        } catch (throwable: Throwable) {
            DeviceTestOutcome.fail(DeviceTestMessage.Failed, throwable.javaClass.simpleName)
        }
    }
}
