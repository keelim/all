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
import com.google.gson.Gson

inline fun <reified ENTITY> Context.loadJson(gson: Gson, fileName: String): ENTITY {
  val result = assets.open(fileName)
    .bufferedReader()
    .use { it.readText() }
  return gson.fromJson(result, ENTITY::class.java)
}
