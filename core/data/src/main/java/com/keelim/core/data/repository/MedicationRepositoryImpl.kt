package com.keelim.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.keelim.data.json.JsonParser
import com.keelim.data.json.decodeOrNull
import com.keelim.data.json.encode
import com.keelim.data.model.Medication
import com.keelim.data.repository.MedicationRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.medicationDataStore: DataStore<Preferences> by preferencesDataStore(name = "medication_prefs")

@Singleton
class MedicationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: JsonParser
) : MedicationRepository {

    override fun getMedications(): Flow<List<Medication>> {
        return context.medicationDataStore.data.map { preferences ->
            val medicationsJson = preferences[MEDICATIONS_KEY]
            if (medicationsJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(medicationsJson).orEmpty()
            }
        }
    }

    override suspend fun addMedication(medication: Medication) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[MEDICATIONS_KEY]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications + medication
            preferences[MEDICATIONS_KEY] = json.encode(updatedMedications)
        }
    }

    override suspend fun updateMedication(medication: Medication) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[MEDICATIONS_KEY]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications.map {
                if (it.id == medication.id) medication else it
            }
            preferences[MEDICATIONS_KEY] = json.encode(updatedMedications)
        }
    }

    override suspend fun removeMedication(id: String) {
        context.medicationDataStore.edit { preferences ->
            val currentJson = preferences[MEDICATIONS_KEY]
            val currentMedications = if (currentJson.isNullOrEmpty()) {
                emptyList()
            } else {
                json.decodeOrNull<List<Medication>>(currentJson).orEmpty()
            }
            val updatedMedications = currentMedications.filter { it.id != id }
            preferences[MEDICATIONS_KEY] = json.encode(updatedMedications)
        }
    }

    override suspend fun getMedicationById(id: String): Medication? {
        return context.medicationDataStore.data.map { preferences ->
            val medicationsJson = preferences[MEDICATIONS_KEY]
            if (medicationsJson.isNullOrEmpty()) {
                null
            } else {
                json.decodeOrNull<List<Medication>>(medicationsJson)?.find { it.id == id }
            }
        }.firstOrNull()
    }

    companion object {
        private val MEDICATIONS_KEY = stringPreferencesKey("medications")
    }
}
