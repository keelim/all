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
package com.keelim.common.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Context.buildIntent(
  vararg argument: Pair<String, Any?>
): Intent = Intent(this, T::class.java).apply {
  putExtras(bundleOf(*argument))
}

inline fun <reified T : Activity> Context.startActivity(
  vararg argument: Pair<String, Any?>
) {
  startActivity(buildIntent<T>(*argument))
}

fun Int.dp2px() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, message, length).show()
}

fun Context.toast(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, message, length).show()
}
