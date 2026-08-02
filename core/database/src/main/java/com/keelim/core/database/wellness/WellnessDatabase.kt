package com.keelim.core.database.wellness

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "measurements")
data class MeasurementEntity(
    @PrimaryKey val localDate: String,
    val lengthCm: Double,
    val circumferenceCm: Double,
    val state: String,
)

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val kind: String,
    val createdLocalDate: String,
)

@Entity(
    tableName = "routine_completions",
    primaryKeys = ["routineId", "localDate"],
    foreignKeys = [
        ForeignKey(
            entity = RoutineEntity::class,
            parentColumns = ["id"],
            childColumns = ["routineId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("routineId")],
)
data class RoutineCompletionEntity(
    val routineId: Long,
    val localDate: String,
    val durationMinutes: Int? = null,
)

@Entity(tableName = "wellness_goal")
data class WellnessGoalEntity(
    @PrimaryKey val id: Int = 0,
    val metric: String,
    val targetCm: Double,
    val baselineCm: Double,
)

@Entity(tableName = "daily_check_ins")
data class DailyCheckInEntity(
    @PrimaryKey val localDate: String,
    val sleep: Int,
    val stress: Int,
    val energy: Int,
    val desire: Int,
    val confidence: Int,
    val morningCondition: String,
    val drankAlcohol: Boolean,
    val didCardio: Boolean,
    val hasDiscomfort: Boolean,
    val note: String,
)

@Entity(tableName = "care_check_ins")
data class CareCheckInEntity(
    @PrimaryKey val localDate: String,
    val condition: String,
    val sweat: Boolean,
    val friction: Boolean,
    val dryness: Boolean,
)

@Dao
interface WellnessDao {
    @Query("SELECT * FROM measurements ORDER BY localDate DESC")
    fun observeMeasurements(): Flow<List<MeasurementEntity>>

    @Query("SELECT * FROM routines ORDER BY createdLocalDate, id")
    fun observeRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routine_completions ORDER BY localDate DESC, routineId")
    fun observeRoutineCompletions(): Flow<List<RoutineCompletionEntity>>

    @Query("SELECT * FROM wellness_goal WHERE id = 0")
    fun observeGoal(): Flow<WellnessGoalEntity?>

    @Query("SELECT * FROM daily_check_ins ORDER BY localDate DESC")
    fun observeCheckIns(): Flow<List<DailyCheckInEntity>>

    @Query("SELECT * FROM care_check_ins ORDER BY localDate DESC")
    fun observeCareCheckIns(): Flow<List<CareCheckInEntity>>

    @Upsert
    suspend fun upsertMeasurement(measurement: MeasurementEntity)

    @Upsert
    suspend fun upsertGoal(goal: WellnessGoalEntity)

    @Query("DELETE FROM wellness_goal")
    suspend fun deleteGoal()

    @Upsert
    suspend fun upsertCheckIn(checkIn: DailyCheckInEntity)

    @Query("DELETE FROM daily_check_ins WHERE localDate = :localDate")
    suspend fun deleteCheckIn(localDate: String)

    @Upsert
    suspend fun upsertCareCheckIn(checkIn: CareCheckInEntity)

    @Insert
    suspend fun insertRoutine(routine: RoutineEntity): Long

    @Insert
    suspend fun insertRoutines(routines: List<RoutineEntity>)

    @Delete
    suspend fun deleteRoutine(routine: RoutineEntity)

    @Upsert
    suspend fun upsertRoutineCompletion(completion: RoutineCompletionEntity)

    @Delete
    suspend fun deleteRoutineCompletion(completion: RoutineCompletionEntity)
}

@Database(
    entities = [
        MeasurementEntity::class,
        RoutineEntity::class,
        RoutineCompletionEntity::class,
        WellnessGoalEntity::class,
        DailyCheckInEntity::class,
        CareCheckInEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class WellnessDatabase : RoomDatabase() {
    abstract fun wellnessDao(): WellnessDao

    companion object {
        const val NAME = "wellness.db"

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `wellness_goal` (
                        `id` INTEGER NOT NULL,
                        `metric` TEXT NOT NULL,
                        `targetCm` REAL NOT NULL,
                        `baselineCm` REAL NOT NULL,
                        PRIMARY KEY(`id`)
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `daily_check_ins` (
                        `localDate` TEXT NOT NULL,
                        `sleep` INTEGER NOT NULL,
                        `stress` INTEGER NOT NULL,
                        `energy` INTEGER NOT NULL,
                        `desire` INTEGER NOT NULL,
                        `confidence` INTEGER NOT NULL,
                        `morningCondition` TEXT NOT NULL,
                        `drankAlcohol` INTEGER NOT NULL,
                        `didCardio` INTEGER NOT NULL,
                        `hasDiscomfort` INTEGER NOT NULL,
                        `note` TEXT NOT NULL,
                        PRIMARY KEY(`localDate`)
                    )
                    """.trimIndent(),
                )
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `care_check_ins` (
                        `localDate` TEXT NOT NULL,
                        `condition` TEXT NOT NULL,
                        `sweat` INTEGER NOT NULL,
                        `friction` INTEGER NOT NULL,
                        `dryness` INTEGER NOT NULL,
                        PRIMARY KEY(`localDate`)
                    )
                    """.trimIndent(),
                )
            }
        }
    }
}
