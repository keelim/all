package com.keelim.shared.di

import com.keelim.shared.data.JsonUserStateStore
import com.keelim.shared.data.UserStateStore
import java.io.File
import okio.FileSystem

actual class Module {
    actual fun createUserStateStore(): UserStateStore {
        val stateDirectory = File(System.getProperty("user.home"), ".all")
            .apply { mkdirs() }
        return JsonUserStateStore(
            fileSystem = FileSystem.SYSTEM,
        ) {
            File(stateDirectory, "userState.json").absolutePath
        }
    }
}
