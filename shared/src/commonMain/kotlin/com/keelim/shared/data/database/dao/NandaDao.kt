package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.keelim.shared.data.database.model.NandaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NandaDao {
    @Query("SELECT * FROM nanda")
    fun getNandaEntities(): Flow<List<NandaEntity>>

    @Query("SELECT * FROM nanda WHERE reason LIKE '%' || :query || '%'")
    fun getDiagnosis(query: String): Flow<List<NandaEntity>>
}
