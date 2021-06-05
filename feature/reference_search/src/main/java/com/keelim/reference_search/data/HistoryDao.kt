package com.keelim.reference_search.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HistoryDao {
    @Query("select * from history")
    fun getAll():List<History>

    @Insert
    fun insertHistory(history:History)

    @Query("delete from history where keyword = :keyword")
    fun delete(keyword:String)
}