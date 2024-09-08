package com.keelim.shared

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
}

actual fun getPlatform(): Platform = DesktopPlatform()

class DesktopAppSupported : AppSupported {
    override val isSupported: Boolean
        get() = false
}
actual fun getAppSupported(): AppSupported = DesktopAppSupported()
