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
package com.keelim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import com.keelim.common.util.startActivity
import com.keelim.nandadiagnosis.compose.ui.setThemeContent
import com.keelim.ui.Section
import com.keelim.ui.SettingScreen

class SettingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setThemeContent {
            val context = LocalContext.current

            SettingScreen { action ->
                val route = when (action) {
                    Section.Developer -> Section.Developer
                }

                context.startActivity<SettingActivity>(
                    "type" to route
                )
            }
        }
    }
}
