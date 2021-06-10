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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.cnubus.ui.OpenSourceActivity
import com.keelim.cnubus.ui.content.Content2Activity
import com.keelim.cnubus.ui.setting.theme.AppTheme.Companion.THEME_ARRAY
import com.keelim.cnubus.utils.MaterialDialog
import com.keelim.cnubus.utils.MaterialDialog.Companion.negativeButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.positiveButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.singleChoiceItems
import com.keelim.cnubus.utils.MaterialDialog.Companion.title
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        readyReview()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initAppThemeObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean { // preference 클릭 리스너
        when (preference.key) {
            "content" -> {
                startActivity(
                    Intent(
                        requireActivity(),
                        Content2Activity::class.java
                    )
                )
            }

            "homepage" -> {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.notification_uri))
                    )
                )
            }

            "opensource" -> startActivity(Intent(requireActivity(), OpenSourceActivity::class.java))

            "update" -> {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.updateLink))
                })
            }

            "theme" -> selectTheme()
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

    private fun initAppThemeObserver() {
        mainViewModel.theme.observe(
            viewLifecycleOwner,
            { theme ->
                val nextTheme = THEME_ARRAY.firstOrNull {
                    it.modeNight == theme
                }
            }
        )
    }

    private fun selectTheme() {
        val currentTheme = mainViewModel.theme.value
        var checkedItem = THEME_ARRAY.indexOfFirst { it.modeNight == currentTheme }
        if (checkedItem >= 0) {
            val items = THEME_ARRAY.map { theme -> getText(theme.modeNameRes) }.toTypedArray()

            MaterialDialog.createDialog(requireContext()) {
                title(R.string.choose_theme)
                singleChoiceItems(items, checkedItem) {
                    checkedItem = it
                }
                positiveButton(getString(R.string.ok)) {
                    val mode = THEME_ARRAY[checkedItem].modeNight
                    AppCompatDelegate.setDefaultNightMode(mode)
                    mainViewModel.setAppTheme(mode)
                }
                negativeButton(getString(R.string.cancel))
            }.show()
        }
    }
}
