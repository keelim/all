interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

interface AppSupported {
    val isSupported: Boolean
}

expect fun getAppSupported(): AppSupported
