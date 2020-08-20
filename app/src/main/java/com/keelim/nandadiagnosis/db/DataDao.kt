package com.keelim.nandadiagnosis.db

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DataDao {
    // todo 중복되는 결과가 존재한다. 수정 필요
    @Query("select * from nanda where reason like :keyword ")
    fun search(keyword: String?): List<NandaEntity>

}