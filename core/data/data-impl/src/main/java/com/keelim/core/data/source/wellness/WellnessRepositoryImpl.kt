package com.keelim.core.data.source.wellness

import android.content.Context
import android.content.SharedPreferences
import com.keelim.common.Dispatcher
import com.keelim.common.KeelimDispatchers
import com.keelim.core.database.wellness.MeasurementEntity
import com.keelim.core.database.wellness.CareCheckInEntity
import com.keelim.core.database.wellness.DailyCheckInEntity
import com.keelim.core.database.wellness.RoutineCompletionEntity
import com.keelim.core.database.wellness.RoutineEntity
import com.keelim.core.database.wellness.WellnessDao
import com.keelim.core.database.wellness.WellnessGoalEntity
import com.keelim.data.repository.WellnessRepository
import com.keelim.model.wellness.Measurement
import com.keelim.model.wellness.CareCheckIn
import com.keelim.model.wellness.CareCondition
import com.keelim.model.wellness.DailyCheckIn
import com.keelim.model.wellness.MorningCondition
import com.keelim.model.wellness.Routine
import com.keelim.model.wellness.RoutineCompletion
import com.keelim.model.wellness.WellnessData
import com.keelim.model.wellness.WellnessGoal
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
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

    override val data: Flow<WellnessData> =
        combine(
            dao.observeMeasurements(),
            dao.observeRoutines(),
            dao.observeRoutineCompletions(),
            dao.observeGoal(),
        ) { measurements, routines, completions, goal ->
            WellnessData(
                measurements = measurements.map(MeasurementEntity::toModel),
                routines = routines.map(RoutineEntity::toModel),
                completions = completions.map(RoutineCompletionEntity::toModel),
                goal = goal?.toModel(),
            )
        }.let { coreData ->
            combine(
                coreData,
                dao.observeCheckIns(),
                dao.observeCareCheckIns(),
            ) { current, checkIns, careCheckIns ->
                current.copy(
                    checkIns = checkIns.map(DailyCheckInEntity::toModel),
                    careCheckIns = careCheckIns.mapNotNull(CareCheckInEntity::toModel),
                )
            }
        }

    override suspend fun initializeDefaultRoutines(createdLocalDate: String) {
        withContext(ioDispatcher) {
            if (sharedPreferences.getBoolean(KEY_DEFAULTS_INITIALIZED, false)) return@withContext
            if (!sharedPreferences.getBoolean(KEY_ONBOARDING_ACCEPTED, false)) {
                dao.insertRoutines(
                    listOf(
                        RoutineEntity(name = "Vitamin", kind = "SUPPLEMENT", createdLocalDate = createdLocalDate),
                        RoutineEntity(name = "20-minute walk", kind = "RUNNING", createdLocalDate = createdLocalDate),
                        RoutineEntity(name = "Light strength training", kind = "EXERCISE", createdLocalDate = createdLocalDate),
                    ),
                )
            }
            sharedPreferences.edit().putBoolean(KEY_DEFAULTS_INITIALIZED, true).apply()
        }
    }

    override suspend fun upsertMeasurement(measurement: Measurement) {
        dao.upsertMeasurement(measurement.toEntity())
    }

    override suspend fun upsertGoal(goal: WellnessGoal) {
        dao.upsertGoal(goal.toEntity())
    }

    override suspend fun deleteGoal() {
        dao.deleteGoal()
    }

    override suspend fun upsertCheckIn(checkIn: DailyCheckIn) {
        dao.upsertCheckIn(checkIn.toEntity())
    }

    override suspend fun deleteCheckIn(localDate: String) {
        dao.deleteCheckIn(localDate)
    }

    override suspend fun upsertCareCheckIn(checkIn: CareCheckIn) {
        dao.upsertCareCheckIn(checkIn.toEntity())
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
        const val KEY_DEFAULTS_INITIALIZED = "wellness_defaults_initialized"
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

private fun WellnessGoalEntity.toModel() = WellnessGoal(metric, targetCm, baselineCm)

private fun WellnessGoal.toEntity() = WellnessGoalEntity(metric = metric, targetCm = targetCm, baselineCm = baselineCm)

private fun DailyCheckInEntity.toModel() = DailyCheckIn(
    localDate = localDate,
    sleep = sleep,
    stress = stress,
    energy = energy,
    desire = desire,
    confidence = confidence,
    morningCondition = MorningCondition.valueOf(morningCondition),
    drankAlcohol = drankAlcohol,
    didCardio = didCardio,
    hasDiscomfort = hasDiscomfort,
    note = note,
)

private fun DailyCheckIn.toEntity() = DailyCheckInEntity(
    localDate = localDate,
    sleep = sleep,
    stress = stress,
    energy = energy,
    desire = desire,
    confidence = confidence,
    morningCondition = morningCondition.name,
    drankAlcohol = drankAlcohol,
    didCardio = didCardio,
    hasDiscomfort = hasDiscomfort,
    note = note,
)

private fun CareCheckInEntity.toModel(): CareCheckIn? =
    runCatching {
        CareCheckIn(
            localDate = localDate,
            condition = CareCondition.valueOf(condition),
            sweat = sweat,
            friction = friction,
            dryness = dryness,
        )
    }.getOrNull()

private fun CareCheckIn.toEntity() = CareCheckInEntity(
    localDate = localDate,
    condition = condition.name,
    sweat = sweat,
    friction = friction,
    dryness = dryness,
)
