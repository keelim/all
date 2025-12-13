package com.keelim.shared.data.model

enum class ThemeType {
    LIGHT,
    DARK;

    fun isDarkTheme(): Boolean = this == DARK
}
