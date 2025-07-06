package com.keelim.web

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent

fun Context.navigateToWebModule(
    uri: Uri,
) {
    CustomTabsIntent.Builder()
        .build()
        .launchUrl(this, uri)
}
