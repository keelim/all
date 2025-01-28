package com.keelim.shared

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.keelim.core.string.Word

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = Word.project.key,
        ) {
        }
    }
}
