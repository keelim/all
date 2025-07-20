package com.keelim.core.data.source.length

import com.keelim.core.database.mapper.toDomain
import com.keelim.core.database.mapper.toEntity
import com.keelim.data.repository.LengthRepository
import com.keelim.model.LengthRecord
import com.keelim.shared.data.database.dao.LengthRecordDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LengthRepositoryImpl @Inject constructor(
    private val dao: LengthRecordDao
) : LengthRepository {
    override fun getAllRecords(): Flow<List<LengthRecord>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override suspend fun addRecord(record: LengthRecord) =
        dao.insert(record.toEntity())

    override suspend fun deleteRecord(date: String) = dao.deleteByDate(date)
}
