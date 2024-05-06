package com.keelim.shared

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

class AndroidAppSupported : AppSupported {
    override val isSupported: Boolean
        get() = false
}
actual fun getAppSupported(): AppSupported = AndroidAppSupported()
