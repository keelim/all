package com.keelim.data.model.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.keelim.data.UserSettings
import timber.log.Timber
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class UserSettingsSerializer @Inject constructor() : Serializer<UserSettings> {
    override val defaultValue: UserSettings = UserSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            UserSettings.parseFrom(input)
        } catch (throwable: Throwable) {
            Timber.e(throwable.toString())
            throw CorruptionException("Cannot read proto, ", throwable)
        }
    }

    override suspend fun writeTo(t: UserSettings, output: OutputStream) {
        t.writeTo(output)
    }
}
