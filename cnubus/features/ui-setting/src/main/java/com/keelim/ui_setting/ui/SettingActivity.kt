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
package com.keelim.ui_setting.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ui.components.Navigation
import com.keelim.ui_setting.ui.theme.CnubusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : ComponentActivity() {

    private val type by lazy {
        (intent.extras?.get("type") as Section?) ?: Section.Developer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeContent {
            CnubusTheme {
                Navigation(
                    path = type,
                    onBackAction = { onBackPressed() }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewSettingActivity() {
    CnubusTheme {
        Navigation(
            path = Section.Developer,
            onBackAction = { },
        )
    }
}
