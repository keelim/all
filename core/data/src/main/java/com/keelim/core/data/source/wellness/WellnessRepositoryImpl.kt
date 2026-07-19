package com.keelim.core.data.source.wellness

import android.content.Context
import android.content.SharedPreferences
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.core.database.wellness.MeasurementEntity
import com.keelim.core.database.wellness.RoutineCompletionEntity
import com.keelim.core.database.wellness.RoutineEntity
import com.keelim.core.database.wellness.WellnessDao
import com.keelim.data.repository.WellnessRepository
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

@Singleton
class WellnessRepositoryImpl internal constructor(
    private val dao: WellnessDao,
    private val sharedPreferences: SharedPreferences,
    private val ioDispatcher: CoroutineDispatcher,
) : WellnessRepository {
    @Inject
    constructor(
        dao: WellnessDao,
        @ApplicationContext context: Context,
        @Dispatcher(KeelimDispatchers.IO) ioDispatcher: CoroutineDispatcher,
    ) : this(
        dao = dao,
        sharedPreferences =
            context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE),
        ioDispatcher = ioDispatcher,
    )

    private val preferences = MutableStateFlow(preferencesSnapshot())

    override val data: Flow<WellnessData> =
        combine(
            dao.observeMeasurements(),
            dao.observeRoutines(),
            dao.observeRoutineCompletions(),
            preferences,
        ) { measurements, routines, completions, currentPreferences ->
            WellnessData(
                measurements = measurements.map(MeasurementEntity::toModel),
                routines = routines.map(RoutineEntity::toModel),
                completions = completions.map(RoutineCompletionEntity::toModel),
                preferences = currentPreferences,
            )
        }

    override fun preferencesSnapshot(): WellnessPreferences =
        WellnessPreferences(
            onboardingAccepted =
                sharedPreferences.getBoolean(KEY_ONBOARDING_ACCEPTED, false),
        )

    override suspend fun setOnboardingAccepted(accepted: Boolean) {
        withContext(ioDispatcher) {
            sharedPreferences.edit().putBoolean(KEY_ONBOARDING_ACCEPTED, accepted).apply()
        }
        preferences.value = preferencesSnapshot()
    }

    override suspend fun upsertMeasurement(measurement: Measurement) {
        dao.upsertMeasurement(measurement.toEntity())
    }

    override suspend fun insertRoutine(routine: Routine): Long =
        dao.insertRoutine(routine.toEntity())

    override suspend fun deleteRoutine(routine: Routine) {
        dao.deleteRoutine(routine.toEntity())
    }

    override suspend fun upsertRoutineCompletion(completion: RoutineCompletion) {
        dao.upsertRoutineCompletion(completion.toEntity())
    }

    override suspend fun deleteRoutineCompletion(completion: RoutineCompletion) {
        dao.deleteRoutineCompletion(completion.toEntity())
    }

    private companion object {
        const val PREFERENCES_FILE_NAME = "wellness_service_preferences"
        const val KEY_ONBOARDING_ACCEPTED = "wellness_onboarding_accepted"
    }
}

private fun MeasurementEntity.toModel() =
    Measurement(localDate, lengthCm, circumferenceCm, state)

private fun Measurement.toEntity() =
    MeasurementEntity(localDate, lengthCm, circumferenceCm, state)

private fun RoutineEntity.toModel() = Routine(id, name, kind, createdLocalDate)

private fun Routine.toEntity() = RoutineEntity(id, name, kind, createdLocalDate)

private fun RoutineCompletionEntity.toModel() =
    RoutineCompletion(routineId, localDate, durationMinutes)

private fun RoutineCompletion.toEntity() =
    RoutineCompletionEntity(routineId, localDate, durationMinutes)
