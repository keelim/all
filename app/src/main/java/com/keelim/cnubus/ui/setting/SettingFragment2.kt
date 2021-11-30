package com.keelim.cnubus.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.cnubus.R
import com.keelim.cnubus.data.repository.theme.AppTheme
import com.keelim.cnubus.feature.map.MapsActivity
import com.keelim.cnubus.ui.main.MainViewModel
import com.keelim.cnubus.ui.setting.compose.ScreenAction
import com.keelim.cnubus.ui.setting.compose.SettingScreen
import com.keelim.cnubus.ui.subway.SubwayActivity
import com.keelim.cnubus.utils.MaterialDialog
import com.keelim.cnubus.utils.MaterialDialog.Companion.negativeButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.positiveButton
import com.keelim.cnubus.utils.MaterialDialog.Companion.singleChoiceItems
import com.keelim.cnubus.utils.MaterialDialog.Companion.title
import com.keelim.compose.ui.setThemeContent
import com.keelim.ui_setting.ClockActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment2 : Fragment() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setThemeContent {
            SettingScreen { action ->
                when (action) {
                    ScreenAction.Homepage -> startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(getString(R.string.notification_uri))
                            )
                        )

                    ScreenAction.Map -> startActivity(Intent(requireActivity(), MapsActivity::class.java))
                    ScreenAction.Update -> startActivity(
                        Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse(getString(R.string.updateLink))
                        }
                    )
                    ScreenAction.Theme -> selectTheme()
                    ScreenAction.OpenSource -> startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                    ScreenAction.Lab -> startActivity(Intent(requireContext(), ClockActivity::class.java))
                    ScreenAction.Developer ->  startActivity(Intent(requireContext(), SettingActivity::class.java))
                    ScreenAction.Subway -> startActivity(Intent(requireContext(), SubwayActivity::class.java))
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppThemeObserver()
    }

    private fun initAppThemeObserver() {
        mainViewModel.theme.observe(viewLifecycleOwner) { theme ->
            val nextTheme = AppTheme.THEME_ARRAY.firstOrNull {
                it.modeNight == theme
            }
        }
    }

    private fun selectTheme() {
        val currentTheme = mainViewModel.theme.value
        var checkedItem = AppTheme.THEME_ARRAY.indexOfFirst { it.modeNight == currentTheme }
        if (checkedItem >= 0) {
            val items = AppTheme.THEME_ARRAY.map { theme -> getText(theme.modeNameRes) }.toTypedArray()

            MaterialDialog.createDialog(requireContext()) {
                title(R.string.choose_theme)
                singleChoiceItems(items, checkedItem) {
                    checkedItem = it
                }
                positiveButton(getString(R.string.ok)) {
                    val mode = AppTheme.THEME_ARRAY[checkedItem].modeNight
                    AppCompatDelegate.setDefaultNightMode(mode)
                    mainViewModel.setAppTheme(mode)
                }
                negativeButton(getString(R.string.cancel))
            }.show()
        }
    }
}