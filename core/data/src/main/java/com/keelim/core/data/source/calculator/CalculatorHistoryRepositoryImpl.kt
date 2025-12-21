package com.keelim.core.data.source.calculator

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.data.repository.calculator.CalculatorHistoryRepository
import com.keelim.model.CalculatorHistory
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.calculatorDataStore: DataStore<Preferences> by preferencesDataStore(name = "calculator_history")

@Singleton
class CalculatorHistoryRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : CalculatorHistoryRepository {

    private val HISTORY_KEY = stringPreferencesKey("history_list")

    override fun getAllHistory(): Flow<List<CalculatorHistory>> {
        return context.calculatorDataStore.data.map { preferences ->
            val jsonString = preferences[HISTORY_KEY] ?: "[]"
            try {
                Json.decodeFromString<List<CalculatorHistory>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun addHistory(history: CalculatorHistory) {
        context.calculatorDataStore.edit { preferences ->
            val currentListJson = preferences[HISTORY_KEY] ?: "[]"
            val currentList = try {
                Json.decodeFromString<List<CalculatorHistory>>(currentListJson)
            } catch (e: Exception) {
                emptyList()
            }
            val newList = (listOf(history) + currentList).take(50)
            preferences[HISTORY_KEY] = Json.encodeToString(newList)
        }
    }

    override suspend fun clearHistory() {
        context.calculatorDataStore.edit { preferences ->
            preferences.remove(HISTORY_KEY)
        }
    }
}
