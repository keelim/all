package com.keelim.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keelim.core.database.model.DeepLink
import kotlinx.coroutines.flow.Flow

@Dao
interface ArduconDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeepLinkUrl(deepLink: DeepLink)

    @Query("SELECT * FROM deepLink  ORDER BY `timestamp` DESC")
    fun getDeepLinkUrls(): Flow<List<DeepLink>>

    @Delete
    suspend fun deleteDeepLinkUrl(deepLink: DeepLink)
}
