package com.keelim.shared.di

import com.keelim.shared.data.UserStateStore
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual class Module {
    actual fun createUserStateStore(): UserStateStore {
        return UserStateStore(
            fileSystem = FileSystem.SYSTEM,
        ) {
            "${fileDirectory()}/userState.json"
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun fileDirectory(): String {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory).path!!
    }
}
