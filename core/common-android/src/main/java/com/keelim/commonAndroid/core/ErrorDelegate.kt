package com.keelim.commonAndroid.core

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

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
