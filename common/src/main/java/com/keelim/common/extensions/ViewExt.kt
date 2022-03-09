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
package com.keelim.common.extensions

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.callbackFlow

@Px
fun View.dip(dipValue: Float) = context.dip(dipValue)

fun View.toInvisible() {
    visibility = View.INVISIBLE
}

fun View.toVisible() {
    visibility = View.VISIBLE
}

fun View.toGone() {
    visibility = View.GONE
}

fun View.toggleVisibility() {
    if (visibility == View.INVISIBLE) {
        toVisible()
    } else {
        toInvisible()
    }
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun View.snack(@StringRes message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, message, duration).show()
}

fun ViewGroup.allDisableChildEnable(flag: Boolean) {
    (0..childCount).forEach { i ->
        val childView = getChildAt(i)
        childView.isEnabled = flag
        if (childView is ViewGroup) {
            childView.allDisableChildEnable(
                flag
            )
        }
    }
}

fun View.clicks() = callbackFlow<View> {

}
