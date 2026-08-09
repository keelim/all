package com.keelim.composeutil.component.snackbar

import com.keelim.composeutil.component.kui.KuiSnackbar
import com.keelim.composeutil.component.kui.KuiSnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.keelim.composeutil.component.kui.KuiText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun SimpleSnackBar(message: String) {
    val hostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = Unit) { hostState.showSnackbar(message = message) }

    KuiSnackbarHost(hostState = hostState) { snackbarData ->
        KuiSnackbar { KuiText(text = snackbarData.visuals.message) }
    }
}
