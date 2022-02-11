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
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentCRootBinding
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.common.base.BaseFragment
import com.keelim.common.extensions.snak

class CRootFragment : BaseFragment<FragmentCRootBinding, MainViewModel>() {
    private val rootList by lazy { resources.getStringArray(R.array.croot).toList() }
    private val cRecyclerViewAdapter = CRecyclerViewAdapter {
        binding.root.snak("C 노선 지도 업데이트 준비 중입니다.")
    }
    override val layoutResourceId: Int = R.layout.fragment_c_root
    override val viewModel: MainViewModel by viewModels()

    override fun initBeforeBinding() = Unit
    override fun initBinding() {
        initViews()
    }
    override fun initAfterBinding() = Unit

    private fun initViews() = with(binding) {
        lvCroot.run {
            setHasFixedSize(true)
            adapter = cRecyclerViewAdapter.apply {
                submitList(rootList)
            }
            itemAnimator = DefaultItemAnimator()
        }
    }
}
