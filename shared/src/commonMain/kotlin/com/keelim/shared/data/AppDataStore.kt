package com.keelim.shared.data

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.okio.OkioSerializer
import androidx.datastore.core.okio.OkioStorage
import com.keelim.shared.di.json
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import okio.BufferedSink
import okio.BufferedSource
import okio.FileSystem
import okio.Path.Companion.toPath
import okio.use

@Serializable
data class UserState(
    val isFirstUser: Boolean = false,
    val visitedTime: Int = 0,
)

internal object JsonSerializer : OkioSerializer<UserState> {
    override val defaultValue: UserState = UserState()
    override suspend fun readFrom(source: BufferedSource): UserState {
        return json.decodeFromString<UserState>(source.readUtf8())
    }
    override suspend fun writeTo(t: UserState, sink: BufferedSink) {
        sink.use {
            it.writeUtf8(json.encodeToString(UserState.serializer(), t))
        }
    }
}
class UserStateStore(
    fileSystem: FileSystem,
    private val produceFilePath: () -> String,
) {
    private val dataStore = DataStoreFactory.create(
        storage = OkioStorage(
            fileSystem = fileSystem,
            serializer = JsonSerializer,
            producePath = {
                produceFilePath().toPath()
            },
        ),
    )
    val userState: Flow<UserState>
        get() = dataStore.data

    suspend fun updateIsFirstUser(isFirstUser: Boolean) {
        dataStore.updateData {
            it.copy(isFirstUser = isFirstUser)
        }
    }

    suspend fun updateVisitedTime() {
        dataStore.updateData {
            it.copy(visitedTime = it.visitedTime + 1)
        }
    }
}
