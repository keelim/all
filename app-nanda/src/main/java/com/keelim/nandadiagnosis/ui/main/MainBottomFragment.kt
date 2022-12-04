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
package com.keelim.nandadiagnosis.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.databinding.FragmentMainBottomBinding
import com.keelim.nandadiagnosis.utils.AppTheme
import com.keelim.setting.SettingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainBottomFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentMainBottomBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        initAppThemeObserver()
        initViews()
    }

    private fun initAppThemeObserver() {
        mainViewModel.theme.observe(
            viewLifecycleOwner
        ) { currentTheme ->
            val appTheme = AppTheme.THEME_ARRAY.firstOrNull { it.modeNight == currentTheme }
            appTheme?.let {
                binding.themeIcon.setIconResource(it.themeIconRes)
                binding.themeDescription.text = getString(it.modeNameRes)
            }
        }
    }

    private fun initViews() = with(binding) {
        themeOption.setOnClickListener {
            chooseThemeClick()
        }

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
            startActivity(Intent(requireContext(), SettingActivity::class.java))
        }
    }

    private fun chooseThemeClick() {
        val currentTheme = mainViewModel.theme.value
        var checkedItem = AppTheme.THEME_ARRAY.indexOfFirst { it.modeNight == currentTheme }
        if (checkedItem >= 0) {
            val items = AppTheme.THEME_ARRAY.map {
                getText(it.modeNameRes)
            }.toTypedArray()
            callDialog(items, checkedItem)
        }
    }

    private fun callDialog(items: Array<CharSequence>, checkedItem: Int) {
        var checkedItem1 = checkedItem
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.choose_theme)
            .setSingleChoiceItems(items, checkedItem1) { _, value ->
                checkedItem1 = value
            }
            .setPositiveButton(R.string.ok) { _, _ ->
                val mode = AppTheme.THEME_ARRAY[checkedItem1].modeNight
                AppCompatDelegate.setDefaultNightMode(mode)
                mainViewModel.setAppTheme(mode)
                // Update theme description TextView
                binding.themeDescription.text = getString(AppTheme.THEME_ARRAY[checkedItem1].modeNameRes)
            }.setNegativeButton(R.string.cancel) { _, _ -> }
            .create()
            .show()
    }
}
