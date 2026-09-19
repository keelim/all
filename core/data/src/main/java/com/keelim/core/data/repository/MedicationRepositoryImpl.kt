package com.keelim.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.core.data.source.local.StringPreferencesKeyMigration
import com.keelim.core.data.source.local.legacyDataStoreMigration
import com.keelim.data.json.decodeOrNull
import com.keelim.data.model.Medication
import com.keelim.data.repository.MedicationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

internal object MedicationStore {
    const val STORE_NAME = "medication_preferences"
    const val LEGACY_STORE_NAME = "medication_prefs"
    const val LEGACY_MEDICATIONS_KEY_NAME = "medications"
    const val MEDICATIONS_KEY_NAME = "medication_items"
}

internal val Context.medicationDataStore: DataStore<Preferences> by preferencesDataStore(
    name = MedicationStore.STORE_NAME,
    produceMigrations = { context ->
        listOf(
            legacyDataStoreMigration(
                context = context,
                legacyStoreName = MedicationStore.LEGACY_STORE_NAME,
                keyMigrations = listOf(
                    StringPreferencesKeyMigration(
                        oldKeyName = MedicationStore.LEGACY_MEDICATIONS_KEY_NAME,
                        newKeyName = MedicationStore.MEDICATIONS_KEY_NAME,
                    ),
                ),
            ),
        )
    },
)

@Singleton
class MedicationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
) : MedicationRepository {
    private val medicationsKey = stringPreferencesKey(MedicationStore.MEDICATIONS_KEY_NAME)

    override fun getMedications(): Flow<List<Medication>> {
        return context.medicationDataStore.data.map { preferences ->
            val medicationsJson = preferences[medicationsKey]
            if (medicationsJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(medicationsJson).orEmpty()
            }
        }
    }

    override suspend fun addMedication(medication: Medication) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[medicationsKey]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications + medication
            preferences[medicationsKey] = json.encodeToString(updatedMedications)
        }
    }

    override suspend fun updateMedication(medication: Medication) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[medicationsKey]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications.map {
                if (it.id == medication.id) medication else it
            }
            preferences[medicationsKey] = json.encodeToString(updatedMedications)
        }
    }

    override suspend fun removeMedication(id: String) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[medicationsKey]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications.filter { it.id != id }
            preferences[medicationsKey] = json.encodeToString(updatedMedications)
        }
    }

    override suspend fun getMedicationById(id: String): Medication? {
        return context.medicationDataStore.data.map { preferences ->
            val medicationsJson = preferences[medicationsKey]
            if (medicationsJson.isNullOrEmpty()) {
                null
            } else {
                json.decodeOrNull<List<Medication>>(medicationsJson)?.find { it.id == id }
            }
        }.firstOrNull()
    }
}
