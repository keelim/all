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
package com.keelim.comssa.ui.main.bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.comssa.databinding.BottomSheetDialogBinding
import com.keelim.comssa.ui.favorite.FavoriteActivity
import com.keelim.comssa.utils.toast
import com.keelim.data.di.download.DownloadReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var recevier: DownloadReceiver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViews() = with(binding) {
        downloadButton.setOnClickListener {
            requireActivity().toast("기능 준비중입니다.")
        }

        btnFavorite.setOnClickListener {
            startActivity(Intent(requireActivity(), FavoriteActivity::class.java))
        }
    }
}
