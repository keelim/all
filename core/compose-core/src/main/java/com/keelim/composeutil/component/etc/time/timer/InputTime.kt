package com.keelim.composeutil.component.etc.time.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun InputTimeDialog(
    title: String,
    hours: String,
    minutes: String,
    seconds: String,
    isPomodoro: Boolean,
    onInputChange: (digit: Int) -> Unit,
    onBackSpace: () -> Unit,
    onSaveLoadedTime: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Card(
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                )

                InputTimeField(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    onBackSpace = onBackSpace
                )

                Spacer(modifier = Modifier.height(8.dp))
                InputKeyboard(onInputChange = onInputChange)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    ButtonLowEmphasis(
                        text = "CANCEL",
                        onClick = onDismiss
                    )
                    ButtonLowEmphasis(
                        text = "SAVE",
                        onClick = { onSaveLoadedTime(isPomodoro) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ButtonLowEmphasis(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Text(
            text = text,
        )
    }
}

@Preview
@Composable
private fun PreviewInputTimeModal() {
    MaterialTheme {
        InputTimeDialog(
            title = "Remind",
            hours = "00",
            minutes = "24",
            seconds = "59",
            isPomodoro = true,
            onInputChange = {},
            onBackSpace = {},
            onSaveLoadedTime = {},
            onDismiss = {},
        )
    }
}
