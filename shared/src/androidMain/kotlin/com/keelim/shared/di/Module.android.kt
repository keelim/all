package com.keelim.shared.di

import android.content.Context
import com.keelim.shared.data.AndroidProtoUserStateStore
import com.keelim.shared.data.UserStateStore

actual class Module(private val context: Context) {
    actual fun createUserStateStore(): UserStateStore {
        return AndroidProtoUserStateStore(
            produceProtoFilePath = {
                context.filesDir.resolve("user_state.pb").absolutePath
            },
            legacyJsonFilePath = {
                context.filesDir.resolve("userState.json").absolutePath
            },
        )
    }
}
