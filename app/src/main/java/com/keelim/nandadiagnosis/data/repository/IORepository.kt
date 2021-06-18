package com.keelim.nandadiagnosis.data.repository;

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2;


interface IORepository {

    suspend fun getNandaList(): List<NandaEntity2>

    suspend fun getLocalNandaList(): List<NandaEntity2>

    suspend fun insertNandaItem(nanda: NandaEntity2): Long

    suspend fun insertNandaList(nanda: List<NandaEntity2>)

    suspend fun updateNanda(nanda: NandaEntity2)

    suspend fun getNandaItem(uid: Long): NandaEntity2?

    suspend fun deleteAll()

    suspend fun deleteNandaItem(uid: Long)
}
