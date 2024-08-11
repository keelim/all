package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keelim.core.database.model.DeepLinkEntity
import com.keelim.core.database.model.SchemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArduconDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeepLinkUrl(deepLink: DeepLinkEntity)

    @Query("SELECT * FROM deepLink  ORDER BY `timestamp` DESC")
    fun getDeepLinkUrls(): Flow<List<DeepLinkEntity>>

    @Query("SELECT * FROM deepLink WHERE url LIKE '%' || :keyword || '%' ORDER BY `timestamp` DESC")
    fun getDeepLinkUrlsFiltered(keyword: String): Flow<List<DeepLinkEntity>>

    @Delete
    suspend fun deleteDeepLinkUrl(deepLink: DeepLinkEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDeepLink(deepLink: DeepLinkEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScheme(scheme: SchemeEntity)

    @Query("SELECT * FROM scheme  ORDER BY `timestamp` DESC")
    fun getSchemeList(): Flow<List<SchemeEntity>>
}
