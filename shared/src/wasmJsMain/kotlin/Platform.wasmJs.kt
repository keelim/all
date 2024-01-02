class WasmPlatform : Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

class WasmAppSupported : AppSupported {
    override val isSupported: Boolean = false
}

actual fun getAppSupported(): AppSupported = WasmAppSupported()
