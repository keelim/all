package com.keelim.commonAndroid.util

import timber.log.Timber


fun Throwable.logError() {
    Timber.e("$message $cause")
}
