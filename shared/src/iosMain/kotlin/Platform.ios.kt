import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

class IOSAppSupported : AppSupported {
    override val isSupported: Boolean = false
}

actual fun getAppSupported(): AppSupported = IOSAppSupported()
