package com.keelim.shared.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.keelim.shared.data.database.model.DeepLinkEntity
import com.keelim.shared.data.database.model.SchemeEntity
import com.keelim.shared.data.database.model.UsageStatEntity
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

    @Query("DELETE FROM scheme WHERE url = :scheme")
    suspend fun deleteScheme(scheme: String)

    @Query("SELECT * FROM scheme  ORDER BY `timestamp` DESC")
    fun getSchemeList(): Flow<List<SchemeEntity>>

    @Query("SELECT DISTINCT category FROM deepLink")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM deepLink ORDER BY usageCount DESC LIMIT :limit")
    fun getTopUsedLinks(limit: Int): List<DeepLinkEntity>

    @Query("SELECT * FROM deepLink ORDER BY lastUsed DESC LIMIT :limit")
    fun getRecentUsedLinks(limit: Int): List<DeepLinkEntity>

    @Query("SELECT strftime('%Y-%m-%d', datetime(lastUsed/1000, 'unixepoch')) as day, SUM(usageCount) as count FROM deepLink GROUP BY day ORDER BY day DESC LIMIT :limit")
    fun getDailyUsageStats(limit: Int): List<UsageStatEntity>
}
