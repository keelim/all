package com.keelim.web

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class KeelimWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        request: WebResourceRequest
    ): Boolean {
        // This ensures that links clicked inside the WebView load within it.
        view.loadUrl(request.url.toString())
        return true
    }
}
