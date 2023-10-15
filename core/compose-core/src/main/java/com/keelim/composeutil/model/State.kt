package com.keelim.composeutil.model

import androidx.compose.runtime.Stable

sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data object Error : Result<Nothing>()
    class Success<T>(t: T?) : Result<T>()
}

@Stable
interface UiState<T : Result<T>> {
    val value: T?
    val exception: Throwable?

    val hasError: Boolean
        get() = exception != null
}
