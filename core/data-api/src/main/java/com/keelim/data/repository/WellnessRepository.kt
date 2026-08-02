package com.keelim.data.repository

import com.keelim.model.wellness.CareCheckIn
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessGoal
import kotlinx.coroutines.flow.Flow

interface WellnessRepository {
    val data: Flow<WellnessData>

    suspend fun initializeDefaultRoutines(createdLocalDate: String)

    suspend fun upsertMeasurement(measurement: Measurement)

    suspend fun upsertGoal(goal: WellnessGoal)

    suspend fun deleteGoal()

    suspend fun upsertCheckIn(checkIn: DailyCheckIn)

    suspend fun deleteCheckIn(localDate: String)

    suspend fun upsertCareCheckIn(checkIn: CareCheckIn)

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun deleteRoutine(routine: Routine)

    suspend fun upsertRoutineCompletion(completion: RoutineCompletion)

    suspend fun deleteRoutineCompletion(completion: RoutineCompletion)
}
