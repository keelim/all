package com.keelim.composeutil.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SimpleDialog(
    text: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "확인")
            }
        },
    )
}
