package com.keelim.shared

import platform.Foundation.NSLocale
import platform.Foundation.preferredLanguages
import platform.UIKit.UIDevice

class ApplePlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val locale: String
        get() = NSLocale
            .preferredLanguages()
            .first()
            .toString()
}

actual fun getPlatform(): Platform = ApplePlatform()

class IOSAppSupported : AppSupported {
    override val isSupported: Boolean = false
}

actual fun getAppSupported(): AppSupported = IOSAppSupported()
