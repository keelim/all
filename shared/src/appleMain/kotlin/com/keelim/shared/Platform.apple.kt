package com.keelim.shared

import platform.UIKit.UIDevice

class ApplePlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = ApplePlatform()

class IOSAppSupported : AppSupported {
    override val isSupported: Boolean = false
}

actual fun getAppSupported(): AppSupported = IOSAppSupported()
