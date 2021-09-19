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
package com.keelim.cnubus.ui.root.croot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentCRootBinding

class CRootFragment : Fragment() {
    private var _binding: FragmentCRootBinding? = null
    private val binding get() = _binding!!
    private val rootList by lazy { resources.getStringArray(R.array.croot).toList() }
    private val cRecyclerViewAdapter = CRecyclerViewAdapter(
        {
            Snackbar.make(binding.root, "C 노선 지도 업데이트 준비 중입니다. ", Snackbar.LENGTH_LONG).show()
        },
        {
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCRootBinding.inflate(inflater, container, false)
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
        lvCroot.setHasFixedSize(true)
        lvCroot.adapter = cRecyclerViewAdapter.apply {
            submitList(rootList)
        }
    }
}
