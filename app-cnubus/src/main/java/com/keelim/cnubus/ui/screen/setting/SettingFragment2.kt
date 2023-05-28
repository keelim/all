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
package com.keelim.cnubus.ui.screen.setting

import android.app.ActivityOptions
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.screen.main.MainViewModel
import com.keelim.cnubus.ui.screen.setting.compose.ScreenAction
import com.keelim.cnubus.ui.screen.setting.compose.SettingScreen
import com.keelim.composeutil.setThemeContent
import com.keelim.map.screen.map1.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

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
                    ScreenAction.Content -> findNavController().navigate(R.id.content3Fragment)
                    ScreenAction.Homepage -> startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(getString(R.string.notification_uri)),
                        ),
                        ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle(),
                    )

                    ScreenAction.Map -> startActivity(
                        Intent(
                            requireActivity(),
                            MapsActivity::class.java,
                        ),
                        ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle(),
                    )
                    ScreenAction.Update -> startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(getString(R.string.updateLink))
                        },
                        ActivityOptions.makeSceneTransitionAnimation(requireActivity()).toBundle(),
                    )
                    ScreenAction.Developer, ScreenAction.Lab -> {
                    }
                    ScreenAction.Subway -> findNavController().navigate(R.id.stationsFragment)
                    ScreenAction.MYPAGE -> findNavController().navigate(R.id.myPageFragment)
                    ScreenAction.AppSetting -> findNavController().navigate(R.id.open_setting_fragment)
                    else -> {}
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeTheme()
    }

    private fun observeTheme() = viewLifecycleOwner.lifecycleScope.launch {
        mainViewModel.theme
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect { theme ->
                Timber.d("Current Theme is $theme")
            }
    }

    companion object {
        fun newInstance(): SettingFragment2 {
            return SettingFragment2().apply {
                arguments = bundleOf()
            }
        }
    }
}
