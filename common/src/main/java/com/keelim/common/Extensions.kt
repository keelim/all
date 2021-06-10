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
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, this.resources.getText(resId), duration).show()
}

fun Context.snack(message: String, duration: Int = Snackbar.LENGTH_SHORT, layout: View) {
    Snackbar.make(layout, message, duration).show()
}

fun Context.snack(@StringRes resId: Int, layout: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(layout, this.resources.getText(resId), duration).show()
}

fun Fragment.snack(message: String, layout: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(layout, message, duration).show()
}

fun Fragment.snack(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT, layout: View) {
    Snackbar.make(layout, this.resources.getText(resId), duration).show()
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireActivity(), message, duration).show()
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireActivity(), this.resources.getText(resId), duration).show()
}



