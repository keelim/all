package com.keelim.comssa.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.keelim.comssa.data.db.entity.Search

@Dao
interface SearchDao {

    @Query("SELECT * FROM Search WHERE title like '%'|| :keyword || '%'")
    fun getSearch(keyword: String): List<Search>

    @Query("UPDATE Search SET favorite=:favorite WHERE id = :id")
    fun favoriteUpdate(favorite:Int, id:Int)

    @Query("SELECT * FROM Search WHERE favorite == 1")
    fun getFavorite(): List<Search>
}