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
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentBRootBinding
import com.keelim.cnubus.feature.map.MapsActivity

class BRootFragment : Fragment(R.layout.fragment_b_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>
    private var fragmentBRootBinding: FragmentBRootBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBRootBinding.bind(view)
        fragmentBRootBinding = binding

        rootList = resources.getStringArray(R.array.broot)
        intentList = resources.getStringArray(R.array.b_intent_array)
//        binding.lvBroot.adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, rootList)

        binding.lvBroot.adapter = BRecyclerViewAdapter(rootList).apply {
            listener = object : BRecyclerViewAdapter.OnRootClickListener {
                override fun onRootClickListener(position: Int) {
                    Toast.makeText(requireActivity(), rootList[position] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

                    Intent(requireActivity(), MapsActivity::class.java).apply {
                        putExtra("location", intentList[position])
                        startActivity(this)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentBRootBinding = null
        super.onDestroyView()
    }
}
