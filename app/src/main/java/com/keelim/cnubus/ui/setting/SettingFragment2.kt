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
package com.keelim.cnubus.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.cnubus.R
import com.keelim.cnubus.data.repository.theme.AppTheme
import com.keelim.cnubus.feature.map.ui.MapsActivity
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.cnubus.ui.setting.compose.ScreenAction
import com.keelim.cnubus.ui.setting.compose.SettingScreen
import com.keelim.common.extensions.repeatCallDefaultOnStarted
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ClockActivity
import com.keelim.ui_setting.ui.SettingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment2 : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setThemeContent {
            SettingScreen { action ->
                when (action) {
                    ScreenAction.MYPAGE -> Unit
                    ScreenAction.Content -> findNavController().navigate(R.id.content3Fragment)
                    ScreenAction.Homepage -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.notification_uri))
                        )
                    )

                    ScreenAction.Map -> startActivity(
                        Intent(
                            requireActivity(),
                            MapsActivity::class.java
                        )
                    )
                    ScreenAction.Update -> startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(getString(R.string.updateLink))
                        }
                    )
                    ScreenAction.Theme -> selectTheme()
                    ScreenAction.OpenSource -> startActivity(
                        Intent(
                            requireContext(),
                            OssLicensesMenuActivity::class.java
                        )
                    )
                    ScreenAction.Lab -> startActivity(
                        Intent(
                            requireContext(),
                            ClockActivity::class.java
                        )
                    )
                    ScreenAction.Developer -> startActivity(
                        Intent(
                            requireContext(),
                            SettingActivity::class.java
                        )
                    )
                    ScreenAction.Subway -> findNavController().navigate(R.id.stationsFragment)
                    ScreenAction.Lab2 -> startActivity(Intent(requireContext(), LabActivity::class.java))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppThemeObserver()
    }

    private fun initAppThemeObserver() = viewLifecycleOwner.lifecycleScope.launch {
        repeatCallDefaultOnStarted {
            mainViewModel.theme.collect { theme ->
                val nextTheme = AppTheme.THEME_ARRAY.firstOrNull {
                    it.modeNight == theme
                }
            }
        }
    }

    private fun selectTheme() = viewLifecycleOwner.lifecycleScope.launch {
        val currentTheme = mainViewModel.theme.last()
        var checkedItem = AppTheme.THEME_ARRAY.indexOfFirst { it.modeNight == currentTheme }
        if (checkedItem >= 0) {
            val items =
                AppTheme.THEME_ARRAY.map { theme -> getText(theme.modeNameRes) }.toTypedArray()

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.choose_theme)
                .setSingleChoiceItems(items, checkedItem) { _, it ->
                    checkedItem = it
                }
                .setPositiveButton(R.string.ok) { _, _ ->
                    val mode = AppTheme.THEME_ARRAY[checkedItem].modeNight
                    AppCompatDelegate.setDefaultNightMode(mode)
                    mainViewModel.setAppTheme(mode)
                }.setNegativeButton(R.string.cancel) { _, _ ->
                }
                .show()
        }
    }
}
