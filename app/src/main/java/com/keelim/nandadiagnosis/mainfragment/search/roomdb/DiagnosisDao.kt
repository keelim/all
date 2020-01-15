package com.keelim.nandadiagnosis.mainfragment.search.roomdb

import androidx.room.Dao
import androidx.room.Query

private const val TABLE_NAME: String = "nanda"
private const val colname = "reason"

@Dao
interface DiagnosisDao {

    @Query("select distinct * from ${TABLE_NAME} where ${colname} like :keyword ")
    fun search(keyword: String?): List<Diagnosis>

}