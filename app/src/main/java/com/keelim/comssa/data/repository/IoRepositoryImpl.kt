package com.keelim.comssa.data.repository

import com.keelim.comssa.data.db.AppDatabase
import com.keelim.comssa.data.db.entity.Search
import com.keelim.comssa.di.IoDispatcher
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class IoRepositoryImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db:AppDatabase,
):IoRepository {
    override suspend fun getSearch(keyword:String): List<Search>  = withContext(ioDispatcher){
        return@withContext db.searchDao.getSearch(keyword)
    }

    override suspend fun updateFavorite(favorite:Int, id:Int) = withContext(ioDispatcher){
        return@withContext db.searchDao.favoriteUpdate(favorite, id)
    }

    override suspend fun getFavorite(): List<Search>  = withContext(ioDispatcher){
        return@withContext db.searchDao.getFavorite()
    }
}