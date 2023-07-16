package com.keelim.setting.screen.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@Composable
fun WebViewRoute(onBackwardClick: () -> Unit, url: String) {
    WebViewScreen(onBackwardClick = onBackwardClick, url = url)
}

@Composable
fun WebViewScreen(onBackwardClick: () -> Unit, url: String) {
    Column {
        val webViewNavigator = rememberWebViewNavigator()
        WebViewNavigationBar(
            onBackwardClick = {
                if (webViewNavigator.canGoBack) {
                    webViewNavigator.navigateBack()
                } else {
                    onBackwardClick()
                }
            },
            onForwardClick = {
                if (webViewNavigator.canGoForward) {
                    webViewNavigator.navigateForward()
                }
            },
            url = url
        )
        val state = rememberWebViewState(url)
        WebView(
            state = state,
            navigator = webViewNavigator,
            modifier = Modifier.fillMaxSize(),
            onCreated = {
                with(it) {
                    settings.run {
                        settings.javaScriptEnabled = true
                    }
                }
            }
        )
        BackHandler(enabled = true) {
            if (webViewNavigator.canGoBack) {
                webViewNavigator.navigateBack()
            } else {
                onBackwardClick()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWebViewScreen() {
    WebViewScreen(
        onBackwardClick = {},
        url = "https://www.google.com/#q=iriure"
    )
}

@Composable
fun WebViewNavigationBar(onBackwardClick: () -> Unit, onForwardClick: () -> Unit, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier =
            Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .clickable { onBackwardClick() },
        )
        Spacer(Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = null,
            modifier =
            Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically)
                .clickable { onForwardClick() },
        )
        Spacer(Modifier.width(24.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = url, maxLines = 1, modifier = Modifier.padding(6.dp))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewWebViewNavigationBar() {
    WebViewNavigationBar(
        onBackwardClick = {},
        onForwardClick = {},
        url = "https://www.google.com/#q=mazim"
    )
}
