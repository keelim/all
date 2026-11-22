package com.keelim.core.data.source.local

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.first
import java.io.File

internal sealed interface PreferencesKeyMigration {
    fun shouldMigrateFromLegacyStore(legacyPreferences: Preferences, currentPreferences: Preferences): Boolean
    fun migrateFromLegacyStore(legacyPreferences: Preferences, targetPreferences: MutablePreferences)
    fun shouldRenameInCurrentStore(currentPreferences: Preferences): Boolean
    fun renameInCurrentStore(targetPreferences: MutablePreferences)
}

internal class StringPreferencesKeyMigration(
    oldKeyName: String,
    newKeyName: String,
) : PreferencesKeyMigration {
    private val oldKey = stringPreferencesKey(oldKeyName)
    private val newKey = stringPreferencesKey(newKeyName)

    override fun shouldMigrateFromLegacyStore(legacyPreferences: Preferences, currentPreferences: Preferences): Boolean {
        return legacyPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun migrateFromLegacyStore(legacyPreferences: Preferences, targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        legacyPreferences[oldKey]?.let { targetPreferences[newKey] = it }
    }

    override fun shouldRenameInCurrentStore(currentPreferences: Preferences): Boolean {
        return currentPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun renameInCurrentStore(targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        targetPreferences[oldKey]?.let { targetPreferences[newKey] = it }
        targetPreferences.remove(oldKey)
    }
}

internal class IntPreferencesKeyMigration(
    oldKeyName: String,
    newKeyName: String,
) : PreferencesKeyMigration {
    private val oldKey = intPreferencesKey(oldKeyName)
    private val newKey = intPreferencesKey(newKeyName)

    override fun shouldMigrateFromLegacyStore(legacyPreferences: Preferences, currentPreferences: Preferences): Boolean {
        return legacyPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun migrateFromLegacyStore(legacyPreferences: Preferences, targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        legacyPreferences[oldKey]?.let { targetPreferences[newKey] = it }
    }

    override fun shouldRenameInCurrentStore(currentPreferences: Preferences): Boolean {
        return currentPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun renameInCurrentStore(targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        targetPreferences[oldKey]?.let { targetPreferences[newKey] = it }
        targetPreferences.remove(oldKey)
    }
}

internal class StringSetPreferencesKeyMigration(
    oldKeyName: String,
    newKeyName: String,
) : PreferencesKeyMigration {
    private val oldKey = stringSetPreferencesKey(oldKeyName)
    private val newKey = stringSetPreferencesKey(newKeyName)

    override fun shouldMigrateFromLegacyStore(legacyPreferences: Preferences, currentPreferences: Preferences): Boolean {
        return legacyPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun migrateFromLegacyStore(legacyPreferences: Preferences, targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        legacyPreferences[oldKey]?.let { targetPreferences[newKey] = it }
    }

    override fun shouldRenameInCurrentStore(currentPreferences: Preferences): Boolean {
        return currentPreferences[oldKey] != null && currentPreferences[newKey] == null
    }

    override fun renameInCurrentStore(targetPreferences: MutablePreferences) {
        if (targetPreferences[newKey] != null) return
        targetPreferences[oldKey]?.let { targetPreferences[newKey] = it }
        targetPreferences.remove(oldKey)
    }
}

internal fun legacyDataStoreMigration(
    context: Context,
    legacyStoreName: String,
    keyMigrations: List<PreferencesKeyMigration>,
): DataMigration<Preferences> = object : DataMigration<Preferences> {
    private val legacyStoreFile by lazy { context.preferencesDataStoreFile(legacyStoreName) }
    private val legacyDataStore by lazy {
        PreferenceDataStoreFactory.create(
            produceFile = { legacyStoreFile },
        )
    }
    private var cachedLegacyPreferences: Preferences? = null

    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        if (!legacyStoreFile.exists()) return false
        val legacyPreferences = readLegacyPreferences()
        return keyMigrations.any { it.shouldMigrateFromLegacyStore(legacyPreferences, currentData) }
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val legacyPreferences = readLegacyPreferences()
        val targetPreferences = currentData.toMutablePreferences()
        keyMigrations.forEach { keyMigration ->
            keyMigration.migrateFromLegacyStore(
                legacyPreferences = legacyPreferences,
                targetPreferences = targetPreferences,
            )
        }
        return targetPreferences
    }

    override suspend fun cleanUp() {
        cachedLegacyPreferences = null
        if (legacyStoreFile.exists()) {
            legacyStoreFile.delete()
        }
    }

    private suspend fun readLegacyPreferences(): Preferences {
        cachedLegacyPreferences?.let { return it }
        val legacyPreferences = runCatching { legacyDataStore.data.first() }
            .getOrElse { emptyPreferences() }
        cachedLegacyPreferences = legacyPreferences
        return legacyPreferences
    }
}

internal fun renameKeysInCurrentStoreMigration(
    keyMigrations: List<PreferencesKeyMigration>,
): DataMigration<Preferences> = object : DataMigration<Preferences> {
    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        return keyMigrations.any { it.shouldRenameInCurrentStore(currentData) }
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val targetPreferences = currentData.toMutablePreferences()
        keyMigrations.forEach { keyMigration ->
            keyMigration.renameInCurrentStore(targetPreferences)
        }
        return targetPreferences
    }

    override suspend fun cleanUp() = Unit
}

internal sealed interface SharedPreferencesKeyMigration {
    fun shouldMigrate(sharedPreferences: SharedPreferences, currentPreferences: Preferences): Boolean
    fun migrate(sharedPreferences: SharedPreferences, targetPreferences: MutablePreferences)
}

internal class SharedPreferencesStringKeyMigration(
    private val sharedPreferencesKeyName: String,
    dataStoreKeyName: String,
) : SharedPreferencesKeyMigration {
    private val dataStoreKey = stringPreferencesKey(dataStoreKeyName)

    override fun shouldMigrate(sharedPreferences: SharedPreferences, currentPreferences: Preferences): Boolean {
        return sharedPreferences.contains(sharedPreferencesKeyName) && currentPreferences[dataStoreKey] == null
    }

    override fun migrate(sharedPreferences: SharedPreferences, targetPreferences: MutablePreferences) {
        if (targetPreferences[dataStoreKey] != null) return
        val value = sharedPreferences.getString(sharedPreferencesKeyName, null) ?: return
        targetPreferences[dataStoreKey] = value
    }
}

internal class SharedPreferencesIntKeyMigration(
    private val sharedPreferencesKeyName: String,
    dataStoreKeyName: String,
) : SharedPreferencesKeyMigration {
    private val dataStoreKey = intPreferencesKey(dataStoreKeyName)

    override fun shouldMigrate(sharedPreferences: SharedPreferences, currentPreferences: Preferences): Boolean {
        return sharedPreferences.contains(sharedPreferencesKeyName) && currentPreferences[dataStoreKey] == null
    }

    override fun migrate(sharedPreferences: SharedPreferences, targetPreferences: MutablePreferences) {
        if (targetPreferences[dataStoreKey] != null) return
        if (!sharedPreferences.contains(sharedPreferencesKeyName)) return
        targetPreferences[dataStoreKey] = sharedPreferences.getInt(sharedPreferencesKeyName, 0)
    }
}

internal fun sharedPreferencesMigration(
    context: Context,
    legacySharedPreferencesName: String,
    keyMigrations: List<SharedPreferencesKeyMigration>,
): DataMigration<Preferences> = object : DataMigration<Preferences> {
    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        if (!context.hasSharedPreferencesFile(legacySharedPreferencesName)) return false
        val sharedPreferences = context.getSharedPreferences(
            legacySharedPreferencesName,
            Context.MODE_PRIVATE,
        )
        return keyMigrations.any { it.shouldMigrate(sharedPreferences, currentData) }
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val sharedPreferences = context.getSharedPreferences(
            legacySharedPreferencesName,
            Context.MODE_PRIVATE,
        )
        val targetPreferences = currentData.toMutablePreferences()
        keyMigrations.forEach { keyMigration ->
            keyMigration.migrate(sharedPreferences, targetPreferences)
        }
        return targetPreferences
    }

    override suspend fun cleanUp() = Unit
}

private fun Context.hasSharedPreferencesFile(name: String): Boolean {
    val sharedPreferencesDirectory = File(applicationInfo.dataDir, "shared_prefs")
    return File(sharedPreferencesDirectory, "$name.xml").exists()
}
