package com.keelim.commonAndroid.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import jakarta.inject.Inject

interface ErrorDelegate {
    val error: SharedFlow<Throwable>
    fun emitError(throwable: Throwable)
}

class ErrorDelegateImpl @Inject constructor() : ErrorDelegate {
    private val _error = MutableSharedFlow<Throwable>()
    override val error: SharedFlow<Throwable> = _error
    override fun emitError(throwable: Throwable) {
        _error.tryEmit(throwable)
    }
}
