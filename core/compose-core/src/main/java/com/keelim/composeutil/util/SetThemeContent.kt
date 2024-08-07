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
package com.keelim.composeutil.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.keelim.composeutil.ui.theme.KeelimTheme

@Suppress("NOTHING_TO_INLINE")
inline fun ComponentActivity.setThemeContent(noinline content: @Composable () -> Unit) =
    setContent {
        KeelimTheme { content() }
    }

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.setThemeContent(noinline content: @Composable () -> Unit) =
    ComposeView(requireActivity()).apply { setContent { KeelimTheme { content() } } }
