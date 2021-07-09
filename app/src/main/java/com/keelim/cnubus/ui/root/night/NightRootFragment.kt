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
package com.keelim.cnubus.ui.root.night

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentNightRootBinding

class NightRootFragment : Fragment(R.layout.fragment_night_root) {
    private  val intentList by lazy{resources.getStringArray(R.array.night1) }
    private val rootList by lazy{resources.getStringArray(R.array.night_intent_array)}
    private var _binding: FragmentNightRootBinding? = null
    private val binding get() = _binding!!

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
        binding.lvNightroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
