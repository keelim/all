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
package com.keelim.common

import androidx.activity.ComponentActivity

inline fun <reified T : Any> ComponentActivity.extra(
    key: String,
    default: T? = null
) = lazy {
    val value = intent.extras?.get(key)
    if (value is String) value else default
}

inline fun <reified T : Any> ComponentActivity.extraNotNull(
    key: String,
    default: T? = null
) = lazy {
    val value = intent.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}
