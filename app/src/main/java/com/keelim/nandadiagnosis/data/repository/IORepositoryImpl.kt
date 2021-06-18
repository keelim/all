package com.keelim.nandadiagnosis.data.repository

import com.keelim.nandadiagnosis.data.db.entity.NandaEntity2
import com.keelim.nandadiagnosis.data.network.NandaService
import com.keelim.nandadiagnosis.di.DefaultDispatcher
import com.keelim.nandadiagnosis.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IORepositoryImpl @Inject constructor(
    private val nandaService: NandaService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
): IORepository {

    override suspend fun getNandaList(): List<NandaEntity2> = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun getLocalNandaList(): List<NandaEntity2> = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun insertNandaItem(nanda: NandaEntity2): Long = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun insertNandaList(nanda: List<NandaEntity2>) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun updateNanda(nanda: NandaEntity2) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun getNandaItem(uid: Long): NandaEntity2? = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }

    override suspend fun deleteNandaItem(uid: Long) = withContext(ioDispatcher){
        TODO("Not yet implemented")
    }
}