/*
 * Designed and developed by 2020 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.commonAndroid.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.keelim.commonAndroid.R

enum class AppTheme(
    val modeNight: Int,
    @DrawableRes val themeIconRes: Int,
    @StringRes val modeNameRes: Int
) {
    FOLLOW_SYSTEM(
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
        R.drawable.baseline_settings_24,
        R.string.system
    ),
    DARK(
        AppCompatDelegate.MODE_NIGHT_YES,
        R.drawable.baseline_dark_mode_24,
        R.string.dark_theme
    ),
    LIGHT(
        AppCompatDelegate.MODE_NIGHT_NO,
        R.drawable.baseline_light_mode_24,
        R.string.light_theme
    );

    companion object {
        val THEME_ARRAY = arrayOf(
            FOLLOW_SYSTEM,
            DARK,
            LIGHT
        )
    }
}
