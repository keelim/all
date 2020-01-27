package com.keelim.cnubus.model.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("select * from location where location1 like :keyword")
    fun location(keyword: String?): List<LocationEntity>
}