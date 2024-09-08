class JsPlatform : Platform {
    override val name: String = "Web with Kotlin/js"
}

actual fun getPlatform(): Platform = JsPlatform()

class JsAppSupported : AppSupported {
    override val isSupported: Boolean = false
}
actual fun getAppSupported(): AppSupported = JsAppSupported()
