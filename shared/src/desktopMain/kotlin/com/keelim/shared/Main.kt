package com.keelim.shared

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.keelim.core.resource.Res
import com.keelim.core.resource.project
import org.jetbrains.compose.resources.stringResource

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.project),
        ) {
        }
    }
}
