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
package com.keelim.cnubus.ui.root.broot

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentBRootBinding
import com.keelim.cnubus.feature.map.MapsActivity
import com.keelim.common.toast

class BRootFragment : Fragment() {
    private val rootList by lazy { resources.getStringArray(R.array.broot).toList() }
    private val intentList by lazy { resources.getStringArray(R.array.b_intent_array).toList() }
    private var _binding: FragmentBRootBinding? = null
    private val binding get() = _binding!!

    private val bRecyclerViewAdapter = BRecyclerViewAdapter(
        shortClickListener = { position ->
            requireActivity().toast(rootList[position] + "정류장 입니다.")
            startActivity(
                Intent(requireContext(), MapsActivity::class.java).apply {
                    putExtra("location", intentList[position])
                }
            )
        },
        longClickListener = {
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBRootBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBRootBinding.bind(view)
        initViews()
    }

    private fun initViews() = with(binding) {
        lvBroot.setHasFixedSize(true)
        lvBroot.adapter = bRecyclerViewAdapter.apply {
            submitList(rootList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
