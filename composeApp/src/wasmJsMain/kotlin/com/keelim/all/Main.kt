@file:OptIn(ExperimentalComposeUiApi::class)

package com.keelim.all

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

fun main() {
    val rootElement = document.getElementById("root") ?: requireNotNull(document.body) {
        "No root element available for compose viewport"
    }
    ComposeViewport(rootElement) {
        App()
    }
}
