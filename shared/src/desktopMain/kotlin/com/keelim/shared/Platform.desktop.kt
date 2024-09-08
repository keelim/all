package com.keelim.shared

import java.util.Locale

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
    override val locale: String
        get() = Locale.getDefault().language
}

actual fun getPlatform(): Platform = DesktopPlatform()

class DesktopAppSupported : AppSupported {
    override val isSupported: Boolean
        get() = false
}
actual fun getAppSupported(): AppSupported = DesktopAppSupported()
