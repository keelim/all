package com.keelim.web

import android.view.ViewGroup
import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebView(
    url: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                webViewClient = KeelimWebViewClient()
                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                }
            }
        },
        modifier = modifier.fillMaxSize(),
        update = { webView ->
            webView.loadUrl(url)
        },
    )
}
