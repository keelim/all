package com.keelim.cnubus.utils

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

interface ErrorViewModel {
    val error: SharedFlow<Throwable>
    fun emitError(throwable: Throwable)
}

class ErrorViewModelImpl @Inject constructor(): ErrorViewModel{
    private val _error = MutableSharedFlow<Throwable>()
    override val error : SharedFlow<Throwable> = _error
    override fun emitError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
        _error.tryEmit(throwable)
    }
}