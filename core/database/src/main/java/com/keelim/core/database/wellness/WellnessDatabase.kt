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

@Entity(tableName = "daily_check_ins")
data class DailyCheckInEntity(
    @PrimaryKey val localDate: String,
    val sleep: Int,
    val stress: Int,
    val energy: Int,
    val desire: Int,
    val confidence: Int,
    val morningCondition: String = "NOT_CHECKED",
    val drankAlcohol: Boolean = false,
    val didCardio: Boolean = false,
    val hasDiscomfort: Boolean = false,
    val note: String = "",
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

@Dao
interface WellnessDao {
    @Query("SELECT * FROM measurements ORDER BY localDate DESC")
    fun observeMeasurements(): Flow<List<MeasurementEntity>>

    @Query("SELECT * FROM daily_check_ins ORDER BY localDate DESC")
    fun observeDailyCheckIns(): Flow<List<DailyCheckInEntity>>

    @Query("SELECT * FROM routines ORDER BY createdLocalDate, id")
    fun observeRoutines(): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routine_completions ORDER BY localDate DESC, routineId")
    fun observeRoutineCompletions(): Flow<List<RoutineCompletionEntity>>

    @Query("SELECT * FROM wellness_goal WHERE id = 0")
    fun observeGoal(): Flow<WellnessGoalEntity?>

    @Upsert
    suspend fun upsertMeasurement(measurement: MeasurementEntity)

    @Upsert
    suspend fun upsertDailyCheckIn(checkIn: DailyCheckInEntity)

    @Upsert
    suspend fun upsertGoal(goal: WellnessGoalEntity)

    @Query("DELETE FROM wellness_goal")
    suspend fun deleteGoal()

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
        DailyCheckInEntity::class,
        RoutineEntity::class,
        RoutineCompletionEntity::class,
        WellnessGoalEntity::class,
    ],
    version = 2,
    exportSchema = true,
)
abstract class WellnessDatabase : RoomDatabase() {
    abstract fun wellnessDao(): WellnessDao

    companion object {
        const val NAME = "wellness.db"

        val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(db: SupportSQLiteDatabase) {
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
                }
            }
    }
}
