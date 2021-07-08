/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
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
package com.keelim.comssa.data.preference

import android.app.Activity
import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceManager @Inject constructor(
  @ApplicationContext context: Context,
) : PreferenceManager {
  private val sharedPreferences = context.getSharedPreferences("preference", Activity.MODE_PRIVATE)
  override fun getString(key: String): String? =
    sharedPreferences.getString(key, null)

  override fun putString(key: String, value: String) {
    sharedPreferences.edit { putString(key, value) }
  }
}
