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
package com.keelim.cnubus.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.OpenSourceActivity
import com.keelim.cnubus.ui.content.Content2Activity
import com.keelim.cnubus.utils.ThemeHelper

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        readyReview()

        val themePreference: ListPreference = findPreference("themePref")!!
        themePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val themeOption = newValue as String
            ThemeHelper.applyTheme(themeOption)
            true
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean { // preference 클릭 리스너
        when (preference.key) {
            "content" -> {
                requireActivity().startActivity(Intent(requireActivity(), Content2Activity::class.java))
            }

            "homepage" -> {
                requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.notification_uri))))
            }

            "opensource" -> {
                startActivity(Intent(requireActivity(), OpenSourceActivity::class.java))
            }

            "update" -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.updateLink))
                    startActivity(this)
                }
            }
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun readyReview() {
        val manager = ReviewManagerFactory.create(requireActivity())

        manager.requestReviewFlow().apply {
            addOnCompleteListener {
                if (this.isSuccessful) {
                    manager.launchReviewFlow(requireActivity(), this.result)
                }
            }
        }
    }
}
