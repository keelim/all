package com.keelim.common.extensions

import kotlinx.coroutines.CoroutineExceptionHandler

val coroutineErrorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    throwable.printStackTrace()
}
