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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentNightRootBinding
import com.keelim.common.extensions.toast

class NightRootFragment : Fragment(R.layout.fragment_night_root) {
    private val rootList by lazy { resources.getStringArray(R.array.night_intent_array).toList() }
    private var _binding: FragmentNightRootBinding? = null
    private val binding get() = _binding!!
    private val nightRecyclerViewAdapter = NightRecyclerViewAdapter(
        click = {
            requireActivity().toast("노선 준비 중입니다. ")
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNightRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() = with(binding) {
        lvNightroot.setHasFixedSize(true)
        lvNightroot.layoutManager = LinearLayoutManager(requireContext())
        lvNightroot.adapter = nightRecyclerViewAdapter.apply {
            submitList(rootList)
        }
    }
}
