package com.keelim.web

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.keelim.common.web.BrowserLauncher

fun Context.asCustomTabsBrowserLauncher(): BrowserLauncher = BrowserLauncher { url ->
    navigateToWebModule(Uri.parse(url))
}

fun Context.navigateToWebModule(
    uri: Uri,
) {
    CustomTabsIntent.Builder()
        .setShowTitle(true)
        .build()
        .launchUrl(this, uri)
}
