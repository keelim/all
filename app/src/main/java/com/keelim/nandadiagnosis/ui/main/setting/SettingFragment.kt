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
package com.keelim.nandadiagnosis.ui.main.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.WebActivity
import com.keelim.nandadiagnosis.ui.kakao_search.SearchActivity
import com.keelim.nandadiagnosis.ui.main.Main2Activity
import com.keelim.nandadiagnosis.ui.main.MainViewModel
import com.keelim.nandadiagnosis.ui.main.setting.theme.AppTheme.Companion.THEME_ARRAY
import com.keelim.nandadiagnosis.ui.open.OpenSourceActivity
import com.keelim.nandadiagnosis.utils.MaterialDialog
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.negativeButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.positiveButton
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.singleChoiceItems
import com.keelim.nandadiagnosis.utils.MaterialDialog.Companion.title
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : PreferenceFragmentCompat() {
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

  override fun onPreferenceTreeClick(preference: Preference?): Boolean {
    when (preference!!.key) {
      "blog" -> {
        Intent(requireActivity(), WebActivity::class.java).apply {
          putExtra("URL", getString(R.string.blog_url))
          startActivity(this)
        }
      }

      "home" -> Toast.makeText(activity, "홈페이지 재구성 중 입니다.", Toast.LENGTH_SHORT).show()

      "opensource" -> startActivity(Intent(context, OpenSourceActivity::class.java))

      "update" -> Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(getString(R.string.urinanda))
        startActivity(this)
      }

      "search" -> requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))

      "theme" -> selectTheme()

      "lab" -> startActivity(Intent(context, Main2Activity::class.java))
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
//        appTheme?.let {
//                    binding.themeIcon.setImageResource(it.themeIconRes)
//                    binding.themeDescription.text = getString(it.modeNameRes)
//                }
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
