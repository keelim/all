package com.keelim.shared.di

import com.keelim.shared.data.UserStateStore
import kotlinx.serialization.json.Json

internal val json = Json { ignoreUnknownKeys = true }

expect class Module {
    fun createUserStateStore(): UserStateStore
}
