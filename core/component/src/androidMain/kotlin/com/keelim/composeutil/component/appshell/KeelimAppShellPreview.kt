package com.keelim.composeutil.component.appshell

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.common.platform.appshell.AppBlockingError
import com.keelim.common.platform.appshell.AppShellState
import com.keelim.common.platform.privacy.PrivacySettings
import com.keelim.common.platform.privacy.PrivacyState
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.theme.KeelimDesignSystemTheme
import com.keelim.core.designsystem.theme.KuiTheme
import java.time.Duration

@Preview(name = "App Shell Content")
@Composable
fun AppShellContentPreview() {
    PreviewTheme {
        KeelimAppShell(
            appState = AppShellState(),
            privacyState = PrivacyState(),
            snackbarHostState = SnackbarHostState(),
            onUnlockRequest = {},
        ) {
            KuiText(
                text = "App content",
                style = KuiTheme.typography.bodyLarge,
                color = KuiTheme.colorScheme.onBackground,
            )
        }
    }
}

@Preview(name = "App Shell Locked")
@Composable
fun AppShellLockedPreview() {
    PreviewTheme {
        KeelimAppShell(
            appState = AppShellState(),
            privacyState = PrivacyState(
                settings = PrivacySettings(
                    appLockEnabled = true,
                    obscureRecentApps = true,
                    blockScreenshots = true,
                    autoLockTimeout = Duration.ZERO,
                ),
                isUnlocked = false,
            ),
            snackbarHostState = SnackbarHostState(),
            onUnlockRequest = {},
        ) {}
    }
}

@Preview(name = "App Shell Blocking Error")
@Composable
fun AppShellBlockingPreview() {
    PreviewTheme {
        KeelimAppShell(
            appState = AppShellState(
                blockingError = AppBlockingError("The app cannot continue."),
            ),
            privacyState = PrivacyState(),
            snackbarHostState = SnackbarHostState(),
            onUnlockRequest = {},
        ) {}
    }
}

@Composable
private fun PreviewTheme(content: @Composable () -> Unit) {
    KeelimDesignSystemTheme(content = content)
}
