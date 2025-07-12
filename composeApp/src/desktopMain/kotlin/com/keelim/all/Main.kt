package com.keelim.all

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState(
        width = 1500.dp,
        height = 1000.dp,
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "ALL",
        state = state,
    ) {
        App()
    }
}
