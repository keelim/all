package com.keelim.data.repository

import com.keelim.model.LengthRecord
import kotlinx.coroutines.flow.Flow

interface LengthRepository {
    fun getAllRecords(): Flow<List<LengthRecord>>
    suspend fun addRecord(record: LengthRecord)
    suspend fun deleteRecord(date: String)
}
