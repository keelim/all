package com.keelim.composeutil.component.appshell

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.WindowManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.keelim.common.platform.appshell.AppShellPresentation
import com.keelim.common.platform.appshell.AppShellState
import com.keelim.common.platform.appshell.resolveAppShellPresentation
import com.keelim.common.platform.privacy.PrivacyState
import com.keelim.core.designsystem.component.KuiButton
import com.keelim.core.designsystem.component.KuiCircularProgressIndicator
import com.keelim.core.designsystem.component.KuiScaffold
import com.keelim.core.designsystem.component.KuiSnackbarHost
import com.keelim.core.designsystem.component.KuiSurface
import com.keelim.core.designsystem.component.KuiText
import com.keelim.core.designsystem.theme.KuiTheme
import com.keelim.core.component.R

@Composable
fun KeelimAppShell(
    appState: AppShellState,
    privacyState: PrivacyState,
    snackbarHostState: SnackbarHostState,
    onUnlockRequest: () -> Unit,
    onBackgrounded: () -> Unit = {},
    onForegrounded: () -> Unit = {},
    blockingContent: (@Composable (String) -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var isForeground by remember {
        mutableStateOf(lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED))
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    isForeground = true
                    onForegrounded()
                }
                Lifecycle.Event.ON_STOP -> {
                    onBackgrounded()
                    isForeground = false
                }
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    ScreenCaptureProtection(privacyState.settings.blockScreenshots)

    KuiScaffold(
        snackbarHost = { KuiSnackbarHost(snackbarHostState) },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (resolveAppShellPresentation(appState, privacyState, isForeground)) {
                AppShellPresentation.CONTENT -> content()
                AppShellPresentation.LOADING -> KuiCircularProgressIndicator(
                    modifier = Modifier.fillMaxSize().wrapContentSize(),
                )
                AppShellPresentation.BLOCKING_ERROR -> {
                    val message = checkNotNull(appState.blockingError).message
                    blockingContent?.invoke(message) ?: DefaultBlockingContent(message)
                }
                AppShellPresentation.LOCKED -> LockedContent(onUnlockRequest)
                AppShellPresentation.OBSCURED -> KuiSurface(
                    modifier = Modifier.fillMaxSize(),
                    color = KuiTheme.colorScheme.surface,
                ) {}
            }
        }
    }
}

@Composable
private fun LockedContent(onUnlockRequest: () -> Unit) {
    KuiSurface(
        modifier = Modifier.fillMaxSize(),
        color = KuiTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            KuiText(
                text = stringResource(R.string.keelim_app_shell_locked),
                style = KuiTheme.typography.titleLarge,
                color = KuiTheme.colorScheme.onSurface,
            )
            KuiButton(
                onClick = onUnlockRequest,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                KuiText(
                    text = stringResource(R.string.keelim_app_shell_unlock),
                    style = KuiTheme.typography.labelLarge,
                    color = KuiTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Composable
private fun DefaultBlockingContent(message: String) {
    KuiSurface(
        modifier = Modifier.fillMaxSize(),
        color = KuiTheme.colorScheme.errorContainer,
    ) {
        KuiText(
            text = message,
            modifier = Modifier.fillMaxSize().wrapContentSize().padding(24.dp),
            style = KuiTheme.typography.bodyLarge,
            color = KuiTheme.colorScheme.onErrorContainer,
        )
    }
}

@Composable
private fun ScreenCaptureProtection(enabled: Boolean) {
    val activity = LocalContext.current.findActivity() ?: return
    DisposableEffect(activity, enabled) {
        val wasSecure = activity.window.attributes.flags and
            WindowManager.LayoutParams.FLAG_SECURE != 0
        if (enabled) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
        onDispose {
            if (wasSecure) {
                activity.window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            } else {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
