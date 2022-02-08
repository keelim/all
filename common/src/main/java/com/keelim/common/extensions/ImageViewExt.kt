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

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load

fun ImageView.loadAsync(
    url: String?,
    @DrawableRes placeholder: Int?
) {
    if (url == null) {
        placeholder?.let { load(it) }
    } else {
        load(url) {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            crossfade(true)
        }
    }
}

fun ImageView.loadAsync(
    uri: Uri?,
    @DrawableRes placeholder: Int?
) {
    if (uri == null) {
        placeholder?.let { load(it) }
    } else {
        load(uri) {
            if (placeholder != null) {
                placeholder(placeholder)
            }
            crossfade(true)
        }
    }
}

fun ImageView.loadAsync(url: String?, doOnEnd: () -> Unit) {
    load(url) {
        listener(
            onSuccess = { _, _ -> doOnEnd() },
            onError = { _, _ -> doOnEnd() }
        )
    }
}
