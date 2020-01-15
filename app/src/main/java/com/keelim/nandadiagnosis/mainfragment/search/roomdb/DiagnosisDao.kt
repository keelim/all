package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DiagnosisDao {

    @Query("select distinct * from nanda where reason like :keyword ")
    fun search(keyword: String?): List<Diagnosis>

}