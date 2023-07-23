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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.keelim.cnubus.R
import com.keelim.composeutil.setThemeContent
import com.keelim.map.screen.map1.MapsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setThemeContent {
            SettingScreen(onScreenAction = ::onSettingAction)
        }
    }

    private fun onSettingAction(action: ScreenAction) = when (action) {
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

        ScreenAction.AppSetting -> findNavController().navigate(R.id.open_setting_fragment)
        else -> Unit
    }
}
