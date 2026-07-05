package com.keelim.arducon.ui.screen.device

enum class DeviceTestStatus {
    Ready,
    Running,
    Pass,
    Fail,
    Skipped,
}

enum class DeviceTestMessage {
    Ready,
    Running,
    CapabilityAvailable,
    PermissionDenied,
    MissingFeature,
    ScannerLaunched,
    ScannerCanceled,
    ScannerResult,
    MicrophoneCaptured,
    VibrationSent,
    SensorsFound,
    NetworkAvailable,
    NetworkUnavailable,
    LocationReady,
    NoLocationProvider,
    IntentLaunched,
    IntentUnavailable,
    Failed,
}

enum class DeviceTestId {
    Camera,
    QrScanner,
    Microphone,
    Vibration,
    Sensors,
    Network,
    Location,
    BrowserIntent,
    ShareIntent,
    AppSettingsIntent,
    NotificationSettingsIntent,
    DialerIntent,
    EmailIntent,
    MapIntent,
}

data class DeviceTestOutcome(
    val status: DeviceTestStatus,
    val message: DeviceTestMessage,
    val detail: String = "",
) {
    companion object {
        fun pass(
            message: DeviceTestMessage,
            detail: String = "",
        ) = DeviceTestOutcome(
            status = DeviceTestStatus.Pass,
            message = message,
            detail = detail,
        )

        fun fail(
            message: DeviceTestMessage,
            detail: String = "",
        ) = DeviceTestOutcome(
            status = DeviceTestStatus.Fail,
            message = message,
            detail = detail,
        )

        fun skipped(
            message: DeviceTestMessage,
            detail: String = "",
        ) = DeviceTestOutcome(
            status = DeviceTestStatus.Skipped,
            message = message,
            detail = detail,
        )
    }
}

data class DeviceTestResult(
    val id: DeviceTestId,
    val status: DeviceTestStatus = DeviceTestStatus.Ready,
    val message: DeviceTestMessage = DeviceTestMessage.Ready,
    val detail: String = "",
)

data class DeviceTestLabUiState(
    val results: Map<DeviceTestId, DeviceTestResult> = DeviceTestId.entries.associateWith {
        DeviceTestResult(id = it)
    },
) {
    val totalCount: Int = results.size
    val passCount: Int = results.values.count { it.status == DeviceTestStatus.Pass }
    val failCount: Int = results.values.count { it.status == DeviceTestStatus.Fail }
    val runningCount: Int = results.values.count { it.status == DeviceTestStatus.Running }
}

internal val hardwareTestIds = listOf(
    DeviceTestId.Camera,
    DeviceTestId.QrScanner,
    DeviceTestId.Microphone,
    DeviceTestId.Vibration,
    DeviceTestId.Sensors,
    DeviceTestId.Network,
    DeviceTestId.Location,
)

internal val systemIntentTestIds = listOf(
    DeviceTestId.BrowserIntent,
    DeviceTestId.ShareIntent,
    DeviceTestId.AppSettingsIntent,
    DeviceTestId.NotificationSettingsIntent,
    DeviceTestId.DialerIntent,
    DeviceTestId.EmailIntent,
    DeviceTestId.MapIntent,
)
