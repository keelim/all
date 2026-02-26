package com.keelim.shared.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.keelim.core.datastore.ThemeType as ProtoThemeType
import com.keelim.core.datastore.UserSetting
import com.keelim.shared.data.model.ThemeType
import com.keelim.shared.di.json
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private object UserSettingSerializer : Serializer<UserSetting> {
    override val defaultValue: UserSetting = UserSetting.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): UserSetting {
        try {
            return UserSetting.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read UserSetting proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: UserSetting,
        output: OutputStream,
    ) {
        t.writeTo(output)
    }
}

class AndroidProtoUserStateStore(
    private val produceProtoFilePath: () -> String,
    private val legacyJsonFilePath: () -> String,
) : UserStateStore {
    private val dataStore = DataStoreFactory.create(
        serializer = UserSettingSerializer,
        produceFile = { File(produceProtoFilePath()) },
        migrations = listOf(
            LegacyJsonUserStateMigration(legacyJsonFilePath),
        ),
    )

    override val userState: Flow<UserState> = dataStore.data.map { it.toUserState() }

    override suspend fun updateIsFirstUser(isFirstUser: Boolean) {
        dataStore.updateData { current ->
            current.toBuilder()
                .setIsFirstUser(isFirstUser)
                .build()
        }
    }

    override suspend fun updateVisitedTime() {
        dataStore.updateData { current ->
            current.toBuilder()
                .setVisitedTime(current.visitedTime + 1)
                .build()
        }
    }

    override val themeTypeFlow: Flow<ThemeType> = dataStore.data.map { userSetting ->
        userSetting.themeType.toThemeType()
    }

    override fun setThemeType(
        value: ThemeType,
        scope: CoroutineScope,
    ) {
        scope.launch {
            dataStore.updateData { current ->
                current.toBuilder()
                    .setThemeType(value.toProtoThemeType())
                    .build()
            }
        }
    }
}

private class LegacyJsonUserStateMigration(
    private val legacyJsonFilePath: () -> String,
) : DataMigration<UserSetting> {
    override suspend fun shouldMigrate(currentData: UserSetting): Boolean {
        if (currentData != UserSetting.getDefaultInstance()) return false
        return File(legacyJsonFilePath()).exists()
    }

    override suspend fun migrate(currentData: UserSetting): UserSetting {
        if (currentData != UserSetting.getDefaultInstance()) return currentData

        val legacyFile = File(legacyJsonFilePath())
        if (!legacyFile.exists()) return currentData

        val migrated = runCatching {
            val legacyState = json.decodeFromString<UserState>(legacyFile.readText())
            legacyState.toUserSetting()
        }.getOrNull()

        return migrated ?: currentData
    }

    override suspend fun cleanUp() {
        val legacyFile = File(legacyJsonFilePath())
        if (legacyFile.exists()) {
            legacyFile.delete()
        }
    }
}

private fun UserSetting.toUserState(): UserState = UserState(
    isFirstUser = isFirstUser,
    visitedTime = visitedTime,
    themeType = themeType.toThemeType(),
)

private fun UserState.toUserSetting(): UserSetting = UserSetting.newBuilder()
    .setIsFirstUser(isFirstUser)
    .setVisitedTime(visitedTime)
    .setThemeType(themeType.toProtoThemeType())
    .build()

private fun ThemeType.toProtoThemeType(): ProtoThemeType = when (this) {
    ThemeType.LIGHT -> ProtoThemeType.THEME_TYPE_LIGHT
    ThemeType.DARK -> ProtoThemeType.THEME_TYPE_DARK
}

private fun ProtoThemeType.toThemeType(): ThemeType = when (this) {
    ProtoThemeType.THEME_TYPE_DARK -> ThemeType.DARK
    ProtoThemeType.THEME_TYPE_LIGHT,
    ProtoThemeType.THEME_TYPE_UNSPECIFIED,
    ProtoThemeType.UNRECOGNIZED -> ThemeType.LIGHT
}
