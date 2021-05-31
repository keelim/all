package com.keelim.cnubus.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.databinding.FragmentMainBottomBinding
import com.keelim.cnubus.ui.setting.theme.AppTheme
import com.keelim.cnubus.utils.MaterialDialog
import com.keelim.cnubus.utils.MaterialDialog.Companion.negativeButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.positiveButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.singleChoiceItems
import com.keelim.cnubus.utils.MaterialDialog.Companion.title
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainBottomFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentMainBottomBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel by viewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBottomBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppThemeObserver()
        setClickListeners()
    }

    private fun initAppThemeObserver() {
        mainViewModel.theme.observe(
            viewLifecycleOwner, { currentTheme ->
                val appTheme = AppTheme.THEME_ARRAY.firstOrNull { it.modeNight == currentTheme }
                appTheme?.let {
                    binding.themeIcon.setImageResource(it.themeIconRes)
                    binding.themeDescription.text = getString(it.modeNameRes)
                }
            }
        )
    }

    private fun setClickListeners() {
        binding.themeOption.setOnClickListener {
            chooseThemeClick()
        }

        binding.openSourceLicensesButton.setOnClickListener {
            dismiss()
            startActivity(Intent(requireContext(), OpenSourceActivity::class.java))
        }

        binding.update.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.updateLink))
                startActivity(this)
            }
        }

        binding.aboutButton.setOnClickListener {
            Snackbar.make(binding.root, "기능 준비 중입니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun chooseThemeClick() {
        val currentTheme = mainViewModel.theme.value
        var checkedItem = AppTheme.THEME_ARRAY.indexOfFirst { it.modeNight == currentTheme }
        if (checkedItem >= 0) {
            val items = AppTheme.THEME_ARRAY.map {
                getText(it.modeNameRes)
            }.toTypedArray()
            MaterialDialog.createDialog(requireContext()) {
                title(R.string.choose_theme)
                singleChoiceItems(items, checkedItem) {
                    checkedItem = it
                }
                positiveButton(getString(R.string.ok)) {
                    val mode = AppTheme.THEME_ARRAY[checkedItem].modeNight
                    AppCompatDelegate.setDefaultNightMode(mode)
                    mainViewModel.setAppTheme(mode)
                    // Update theme description TextView
                    binding.themeDescription.text = getString(AppTheme.THEME_ARRAY[checkedItem].modeNameRes)
                }
                negativeButton(getString(R.string.cancel))
            }.show()
        }
    }
}