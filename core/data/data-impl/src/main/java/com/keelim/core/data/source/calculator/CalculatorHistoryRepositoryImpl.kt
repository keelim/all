package com.keelim.core.data.source.calculator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.data.source.local.StringPreferencesKeyMigration
import com.keelim.core.data.source.local.legacyDataStoreMigration
import com.keelim.data.repository.calculator.CalculatorHistoryRepository
import com.keelim.model.CalculatorHistory
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal object CalculatorStore {
    const val STORE_NAME = "calculator_preferences"
    const val LEGACY_STORE_NAME = "calculator_history"
    const val LEGACY_HISTORY_KEY_NAME = "history_list"
    const val HISTORY_KEY_NAME = "calculator_history_list"
}

val Context.calculatorDataStore: DataStore<Preferences> by preferencesDataStore(
    name = CalculatorStore.STORE_NAME,
    produceMigrations = { context ->
        listOf(
            legacyDataStoreMigration(
                context = context,
                legacyStoreName = CalculatorStore.LEGACY_STORE_NAME,
                keyMigrations = listOf(
                    StringPreferencesKeyMigration(
                        oldKeyName = CalculatorStore.LEGACY_HISTORY_KEY_NAME,
                        newKeyName = CalculatorStore.HISTORY_KEY_NAME,
                    ),
                ),
            ),
        )
    },
)

@Singleton
class CalculatorHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : CalculatorHistoryRepository {

    private val historyKey = stringPreferencesKey(CalculatorStore.HISTORY_KEY_NAME)

    override fun getAllHistory(): Flow<List<CalculatorHistory>> {
        return context.calculatorDataStore.data.map { preferences ->
            val jsonString = preferences[historyKey] ?: "[]"
            try {
                Json.decodeFromString<List<CalculatorHistory>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun addHistory(history: CalculatorHistory) {
        context.calculatorDataStore.edit { preferences ->
            val currentListJson = preferences[historyKey] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<CalculatorHistory>>(currentListJson)
            } catch (e: Exception) {
                emptyList()
            }
            val newList = (listOf(history) + currentList).take(50)
            preferences[historyKey] = Json.encodeToString(newList)
        }
    }

    override suspend fun clearHistory() {
        context.calculatorDataStore.edit { preferences ->
            preferences.remove(historyKey)
        }
    }
}
