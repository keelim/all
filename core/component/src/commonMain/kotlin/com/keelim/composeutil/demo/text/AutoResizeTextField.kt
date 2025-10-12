package com.keelim.composeutil.demo.text

import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AutoResizingTextField(
    modifier: Modifier = Modifier
) {
    val state = rememberTextFieldState()
    BasicTextField(
        state = state,
        modifier = modifier,
        decorator = { _ ->
            BasicText(
                text = state.text.toString(),
                style = MaterialTheme.typography.bodyLarge,
                autoSize = TextAutoSize.StepBased()
            )
        }
    )
}
