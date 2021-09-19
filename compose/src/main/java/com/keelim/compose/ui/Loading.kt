package com.keelim.compose.ui

import androidx.compose.runtime.Composable

@Composable
fun Loading(
    loading: Boolean,
    loadingContent: @Composable () -> Unit,
    error: Boolean = false,
    errorContent: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    when{
        loading -> loadingContent()
        error -> errorContent()
        else -> content()
    }
}

