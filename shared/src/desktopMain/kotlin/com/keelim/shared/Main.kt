package com.keelim.shared

import androidx.compose.ui.window.application
import com.keelim.shared.ui.DesktopWindow

fun main() {
    application {
        DesktopWindow(
            onCloseRequest = ::exitApplication,
        ) {
        }
    }
}
