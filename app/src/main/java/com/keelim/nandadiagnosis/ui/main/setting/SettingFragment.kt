package com.keelim.nandadiagnosis.ui.main.setting


import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.WebActivity
import com.keelim.nandadiagnosis.ui.kakao_search.SearchActivity
import com.keelim.nandadiagnosis.ui.open.OpenSourceActivity
import com.keelim.nandadiagnosis.utils.ThemeHelper


class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var darkPerf: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        readyReview()

        val themePreference: ListPreference = findPreference("themePref")!!
        themePreference.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val themeOption = newValue as String
            ThemeHelper.applyTheme(themeOption)
            true
        }
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        when (preference!!.key) {
            "blog" -> Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.blog_url))
                startActivity(this)
            }

            "home" -> Toast.makeText(activity, "홈페이지 재구성 중 입니다.", Toast.LENGTH_SHORT).show()

            "opensource" -> startActivity(Intent(context, OpenSourceActivity::class.java))

            "update" -> Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.urinanda))
                startActivity(this)
            }

            "search" -> requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun readyReview() {
        val manager = ReviewManagerFactory.create(requireActivity())

        manager.requestReviewFlow().apply {
            addOnCompleteListener {
                if (this.isSuccessful) {
                    manager.launchReviewFlow(requireActivity(), this.result)
                }
            }
        }
    }

    companion object {
        const val DARK = "DARK"
    }
}