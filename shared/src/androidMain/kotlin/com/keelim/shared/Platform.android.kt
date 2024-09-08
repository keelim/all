package com.keelim.shared

import android.os.Build
import java.util.Locale

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val locale: String
        get() = Locale.getDefault().language
}

actual fun getPlatform(): Platform = AndroidPlatform()

class AndroidAppSupported : AppSupported {
    override val isSupported: Boolean
        get() = false
}
actual fun getAppSupported(): AppSupported = AndroidAppSupported()
