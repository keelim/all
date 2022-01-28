package com.keelim.cnubus.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.keelim.cnubus.data.db.entity.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
  @Query("SELECT * FROM Comment")
  suspend fun getAll(): List<Comment>

  @Query("SELECT * FROM Comment")
  fun getAllFlow(): Flow<List<Comment>>

  @Transaction
  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addFavorite(item: Comment)

  @Transaction
  @Delete
  suspend fun removeFavorite(item: Comment)
}
