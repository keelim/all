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
package com.keelim.cnubus.ui.screen.setting.appsetting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.compose.Lab3Activity
import com.keelim.cnubus.ui.screen.setting.LabActivity
import com.keelim.labs.ui.capture.CaptureActivity

class LabFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_lab)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "lab1" -> startActivity(
                Intent(
                    requireContext(),
                    com.keelim.cnubus.ui.screen.setting.LabActivity::class.java
                )
            )
            "lab2" -> startActivity(
                Intent(
                    requireContext(),
                    CaptureActivity::class.java
                )
            )
            "lab3" -> startActivity(
                Intent(
                    requireContext(),
                    Lab3Activity::class.java
                )
            )
        }
        return true
    }
}
