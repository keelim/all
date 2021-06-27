package com.keelim.comssa.data.repository

import com.keelim.comssa.data.db.entity.Search

interface IoRepository {

    suspend fun getSearch(keyword: String): List<Search>

    suspend fun updateFavorite(favorite: Int, id: Int)

    suspend fun getFavorite(): List<Search>
}