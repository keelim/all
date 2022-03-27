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
package com.keelim.cnubus.ui.root.night

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentNightRootBinding
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.common.base.BaseFragment
import com.keelim.common.extensions.toast

class NightRootFragment : BaseFragment<FragmentNightRootBinding, MainViewModel>() {
    private val rootList by lazy { resources.getStringArray(R.array.night_intent_array).toList() }
    private val nightRecyclerViewAdapter = NightRecyclerViewAdapter(
        click = {
            requireActivity().toast("노선 준비 중입니다. ")
        }
    )
    override val layoutResourceId: Int = R.layout.fragment_night_root
    override val viewModel: MainViewModel by viewModels()

    override fun initBeforeBinding() = Unit
    override fun initBinding() {
        initViews()
    }

    override fun initAfterBinding() = Unit

    private fun initViews() = with(binding) {
        lvNightroot.run {
            setHasFixedSize(true)
            adapter = nightRecyclerViewAdapter.apply {
                submitList(rootList)
            }
            itemAnimator = DefaultItemAnimator()
        }
    }

    companion object {
        fun newInstance(): NightRootFragment {
            return NightRootFragment().apply {
                arguments = bundleOf()
            }
        }
    }
}
