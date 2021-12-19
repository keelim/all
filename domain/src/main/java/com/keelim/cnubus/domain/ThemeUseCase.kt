package com.keelim.cnubus.domain

import com.keelim.cnubus.data.repository.theme.ThemeRepository
import kotlinx.coroutines.flow.Flow

class ThemeUseCase(
    private val themeRepository: ThemeRepository
) {
    val appTheme: Flow<Int> = themeRepository.getUserTheme()
    suspend fun setUserTheme(theme: Int) = themeRepository.setUserTheme(theme)
}