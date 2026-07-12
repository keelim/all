package com.keelim.commonAndroid.core

import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
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
        Firebase.crashlytics.recordException(throwable)
        _error.tryEmit(throwable)
    }
}
