package com.keelim.common.platform.appshell

import com.keelim.common.platform.privacy.PrivacyState

data class AppBlockingError(
    val message: String,
)

data class AppShellState(
    val isReady: Boolean = true,
    val blockingError: AppBlockingError? = null,
)

enum class AppShellPresentation {
    CONTENT,
    LOADING,
    BLOCKING_ERROR,
    LOCKED,
    OBSCURED,
}

fun resolveAppShellPresentation(
    appState: AppShellState,
    privacyState: PrivacyState,
    isForeground: Boolean,
): AppShellPresentation = when {
    privacyState.settings.obscureRecentApps && !isForeground -> AppShellPresentation.OBSCURED
    privacyState.settings.appLockEnabled && !privacyState.isUnlocked -> AppShellPresentation.LOCKED
    appState.blockingError != null -> AppShellPresentation.BLOCKING_ERROR
    !appState.isReady -> AppShellPresentation.LOADING
    else -> AppShellPresentation.CONTENT
}
