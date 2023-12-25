class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

class JvmAppSupported : AppSupported {
    override val isSupported: Boolean = false
}

actual fun getAppSupported(): AppSupported = JvmAppSupported()
