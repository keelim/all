package com.keelim.commonAndroid.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.keelim.commonAndroid.R

enum class AppTheme(
    val modeNight: Int,
    @DrawableRes val themeIconRes: Int,
    @StringRes val modeNameRes: Int,
) {
    FOLLOW_SYSTEM(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        R.drawable.baseline_settings_24,
        R.string.system,
    ),
    DARK(
        AppCompatDelegate.MODE_NIGHT_YES,
        R.drawable.baseline_dark_mode_24,
        R.string.dark_theme,
    ),
    LIGHT(
        AppCompatDelegate.MODE_NIGHT_NO,
        R.drawable.baseline_light_mode_24,
        R.string.light_theme,
    );

    companion object {
        val THEME_ARRAY = arrayOf(
            FOLLOW_SYSTEM,
            DARK,
            LIGHT,
        )
    }
}
