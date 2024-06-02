package com.keelim.shared

import com.keelim.res.Res
import com.keelim.res.project_name
import org.jetbrains.compose.resources.getString

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

interface AppSupported {
    val isSupported: Boolean
}

expect fun getAppSupported(): AppSupported

suspend fun getName(): String = getString(Res.string.project_name)
