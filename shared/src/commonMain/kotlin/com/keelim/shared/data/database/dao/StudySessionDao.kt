package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.keelim.shared.data.database.model.StudySession
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
    @Query("SELECT * FROM studySession ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<StudySession>>

    @Query("SELECT * FROM studySession WHERE date BETWEEN :startDate AND :endDate ORDER BY createdAt DESC")
    fun observeByDateRange(startDate: String, endDate: String): Flow<List<StudySession>>

    @Query("SELECT * FROM studySession ORDER BY date DESC")
    fun getDailyAggregated(): Flow<List<StudySession>>

    @Query("SELECT * FROM studySession ORDER BY subject ASC")
    fun getSubjectAggregated(): Flow<List<StudySession>>

    @Query("SELECT SUM(durationSeconds) FROM studySession")
    fun getTotalStudySeconds(): Flow<Int?>

    @Query("SELECT COUNT(DISTINCT date) FROM studySession")
    fun getStudyDaysCount(): Flow<Int?>

    @Upsert
    suspend fun upsert(session: StudySession)

    @Query("DELETE FROM studySession")
    suspend fun deleteAll()
}
