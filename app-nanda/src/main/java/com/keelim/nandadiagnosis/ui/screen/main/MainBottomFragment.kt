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
package com.keelim.nandadiagnosis.ui.screen.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentMainBottomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainBottomFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentMainBottomBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMainBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() = with(binding) {
        aboutButton.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.aboutFragment)
        }

        openSourceLicensesButton.setOnClickListener {
            dismiss()
            startActivity(Intent(requireContext(), OssLicensesActivity::class.java))
        }

        update.setOnClickListener {
            dismiss()
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.urinanda))))
        }

        blog.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.inAppWebFragment)
        }

        login.setOnClickListener {
            dismiss()
            findNavController().navigate(R.id.profileFragment)
        }

        binding.labFeature.setOnClickListener {
            dismiss()
        }
    }
}
