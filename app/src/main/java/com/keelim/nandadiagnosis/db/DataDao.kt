package com.keelim.nandadiagnosis.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DataDao {

    @Query("select * from nanda where reason like  '%' || :keyword || '%'")
    fun search(keyword: String?): List<NandaEntity>

}