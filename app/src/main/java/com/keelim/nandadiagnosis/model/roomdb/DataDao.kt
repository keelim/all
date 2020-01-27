package com.keelim.nandadiagnosis.model.roomdb

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DataDao {
    // todo 중복되는 결과가 존재한다. 수정 필요
    @Query("select * from nanda where reason like :keyword ")
    fun search(keyword: String?): List<NandaEntity>

    @Query("select distinct * from nanda where reason like :keyword")
    fun search2(keyword: String?): List<NandaEntity>
}