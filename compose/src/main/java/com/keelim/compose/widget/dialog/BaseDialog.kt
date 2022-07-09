package com.keelim.compose.widget.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun BaseDialog(
    onDismissRequest: () ->Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
        content = content
    )
}

@Composable
fun SampleDialog(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
) {
    if(isVisible){
        BaseDialog(
            onDismissRequest = onDismissRequest,
            content = {
                Column {
                    Text(
                       modifier = Modifier
                           .padding(top = 24.dp)
                           .padding(horizontal = 12.dp)
                           .clickable { onDismissRequest.invoke() },
                        text = "안녕하세요",
                    )
                }
            }
        )
    }
}
