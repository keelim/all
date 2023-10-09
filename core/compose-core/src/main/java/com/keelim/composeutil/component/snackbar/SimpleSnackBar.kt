package com.keelim.composeutil.component.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SimpleSnackBar(message: String) {
    val hostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = Unit) { hostState.showSnackbar(message = message) }

    SnackbarHost(hostState = hostState) { snackbarData ->
        Snackbar { Text(text = snackbarData.visuals.message) }
    }
}

@Preview
@Composable
fun PreviewSimpleSnackbar() {
    SimpleSnackBar(message = "hello world")
}
