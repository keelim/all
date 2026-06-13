package com.keelim.common.web

fun interface BrowserLauncher {
    fun open(url: String)
}

val NoOpBrowserLauncher = BrowserLauncher {}

