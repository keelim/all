package com.keelim.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import com.keelim.core.resource.Res
import com.keelim.core.resource.project
import org.jetbrains.compose.resources.stringResource

@Composable
fun DesktopWindow(
    onCloseRequest: () -> Unit,
    content: @Composable FrameWindowScope.() -> Unit,
) {
    Window(
        onCloseRequest = onCloseRequest,
        title = stringResource(Res.string.project),
        content = content,
    )
}
