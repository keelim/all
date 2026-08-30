package com.keelim.data.repository

import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.CheckInRecord
import com.keelim.model.wellness.RecoveryGoal
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessGoal
import kotlinx.coroutines.flow.Flow

interface WellnessRepository {
    val data: Flow<WellnessData>

    suspend fun initializeDefaultRoutines(createdLocalDate: String)

    suspend fun upsertMeasurement(measurement: Measurement)

    suspend fun upsertCheckIn(checkIn: CheckInRecord)

    suspend fun upsertGoal(goal: WellnessGoal)

    suspend fun deleteGoal()

    suspend fun upsertRecoveryGoal(goal: RecoveryGoal)

    suspend fun insertRoutine(routine: Routine): Long

    suspend fun deleteRoutine(routine: Routine)

    suspend fun upsertRoutineCompletion(completion: RoutineCompletion)

    suspend fun deleteRoutineCompletion(completion: RoutineCompletion)
}
