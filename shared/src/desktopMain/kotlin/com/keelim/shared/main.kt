package com.keelim.shared

import all.shared.generated.resources.Res
import all.shared.generated.resources.project
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.stringResource

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = stringResource(Res.string.project)
        ) {

        }
    }
}
