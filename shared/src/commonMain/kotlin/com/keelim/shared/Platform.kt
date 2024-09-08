package com.keelim.shared

interface Platform {
    val name: String
    val locale: String
}

expect fun getPlatform(): Platform

interface AppSupported {
    val isSupported: Boolean
}

expect fun getAppSupported(): AppSupported
