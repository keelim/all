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
package com.keelim.cnubus.ui.root.aroot

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentARootBinding
import com.keelim.cnubus.feature.map.MapsActivity

class ARootFragment : Fragment(R.layout.fragment_a_root) {
    private lateinit var rootList: Array<String>
    private lateinit var intentList: Array<String>
    private var fragmentARootBinding: FragmentARootBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentARootBinding.bind(view)
        fragmentARootBinding = binding
        rootList = resources.getStringArray(R.array.aroot)
        intentList = resources.getStringArray(R.array.a_intent_array)

        binding.lvAroot.adapter = ARecyclerViewAdapter(rootList).apply {
            listener = object : ARecyclerViewAdapter.OnRootClickListener {
                override fun onRootClickListener(position: Int) {
                    Toast.makeText(requireActivity(), rootList[position] + "정류장 입니다.", Toast.LENGTH_SHORT).show()

                    Intent(requireActivity(), MapsActivity::class.java).apply {
                        putExtra("location", intentList[position])
                        startActivity(this)
                    }
                }

                override fun onRootLongClickListener(position: Int) {
                }
            }
        }
    }

    override fun onDestroyView() {
        fragmentARootBinding = null
        super.onDestroyView()
    }
}
