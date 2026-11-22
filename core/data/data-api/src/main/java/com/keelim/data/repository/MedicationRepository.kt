package com.keelim.data.repository

import com.keelim.data.model.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun getMedications(): Flow<List<Medication>>
    suspend fun addMedication(medication: Medication)
    suspend fun updateMedication(medication: Medication)
    suspend fun removeMedication(id: String)
    suspend fun getMedicationById(id: String): Medication?
}
