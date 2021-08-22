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
package com.keelim.common

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class ThemeType {
  LIGHT, DARK, DEFAULT
}

object ThemeHelper {
  fun applyTheme(type: ThemeType) {
    val mode = when (type) {
      ThemeType.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
      ThemeType.DARK -> AppCompatDelegate.MODE_NIGHT_YES
      else -> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
          AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        } else {
          AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
        }
      }
    }
    AppCompatDelegate.setDefaultNightMode(mode)
  }

  fun isDarkTheme(context: Context) = context.resources.configuration.uiMode and
    Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

  fun isLightTheme(context: Context) = !isDarkTheme(context)
}
