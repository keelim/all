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
import com.keelim.composeutil.resource.space24
import com.keelim.composeutil.resource.space4
import com.keelim.composeutil.resource.space8

@Composable
fun WebViewRoute(
    uri: String,
    onNavigateCategory: () -> Unit,
) {
    WebViewScreen(
        uri = uri,
        onNavigateCategory = onNavigateCategory,
    )
}

@Composable
fun WebViewScreen(
    uri: String,
    onNavigateCategory: () -> Unit,
) {
    Column {
        val webViewNavigator = rememberWebViewNavigator()
        WebViewNavigationBar(
            onBackwardClick = {
                if (webViewNavigator.canGoBack) {
                    webViewNavigator.navigateBack()
                } else {
                    onNavigateCategory()
                }
            },
            onForwardClick = {
                if (webViewNavigator.canGoForward) {
                    webViewNavigator.navigateForward()
                }
            },
            url = uri,
        )
        val state = rememberWebViewState(uri)
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
            },
        )
        BackHandler(enabled = true) {
            if (webViewNavigator.canGoBack) {
                webViewNavigator.navigateBack()
            } else {
                onNavigateCategory()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWebViewScreen() {
    WebViewScreen(
        uri = "https://www.google.com/#q=iriure",
        onNavigateCategory = {},
    )
}

@Composable
fun WebViewNavigationBar(onBackwardClick: () -> Unit, onForwardClick: () -> Unit, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = space8),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier =
            Modifier
                .size(space24)
                .align(Alignment.CenterVertically)
                .clickable { onBackwardClick() },
        )
        Spacer(Modifier.width(10.dp))
        Icon(
            imageVector = Icons.Rounded.ArrowForward,
            contentDescription = null,
            modifier =
            Modifier
                .size(space24)
                .align(Alignment.CenterVertically)
                .clickable { onForwardClick() },
        )
        Spacer(Modifier.width(space24))
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(modifier = Modifier.width(space4))
                Icon(
                    imageVector = Icons.Rounded.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
                Text(text = url, maxLines = 1, modifier = Modifier.padding(6.dp))
            }
        }
    }
}

@Preview
@Composable
fun PreviewWebViewNavigationBar() {
    WebViewNavigationBar(
        onBackwardClick = {},
        onForwardClick = {},
        url = "https://www.google.com/#q=mazim",
    )
}
