package com.keelim.cnubus.data.repository.theme

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    fun getUserTheme(): Flow<Int>
    suspend fun setUserTheme(theme:Int)
}