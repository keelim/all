package com.keelim.data.repository

import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessPreferences
import kotlinx.coroutines.flow.Flow

interface WellnessRepository {
    val data: Flow<WellnessData>

    fun preferencesSnapshot(): WellnessPreferences

    suspend fun setOnboardingAccepted(accepted: Boolean)

    suspend fun upsertMeasurement(measurement: Measurement)

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun deleteRoutine(routine: Routine)

    suspend fun upsertRoutineCompletion(completion: RoutineCompletion)

    suspend fun deleteRoutineCompletion(completion: RoutineCompletion)
}
