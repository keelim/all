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
package com.keelim.cnubus.ui.screen.root

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.keelim.common.extensions.toast
import com.keelim.composeutil.setThemeContent
import com.keelim.map.screen.map.MapActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootFragment : Fragment() {
    private val viewModel: RootViewModel by viewModels()
    private val mode by lazy { requireArguments().getString("mode") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setThemeContent {
        RootScreen(
            onRootClick = ::moveRoot,
            viewModel = viewModel
        )
    }

    private fun moveRoot(position: Int) {
        val data = when (mode) {
            "a" -> viewModel.data.value[position].roota
            "b" -> viewModel.data.value[position].rootb
            "c" -> viewModel.data.value[position].rootc
            else -> null
        }
        data?.let { value ->
            startActivity(
                Intent(requireContext(), MapActivity::class.java).apply {
                    putExtra("location", value - 1)
                    putExtra("mode", mode)
                },
            )
        } ?: toast("노선 준비 중입니다. ")
    }

    companion object {
        fun newInstance(mode: String): RootFragment {
            return RootFragment().apply {
                arguments = bundleOf(
                    "mode" to mode,
                )
            }
        }
    }
}
