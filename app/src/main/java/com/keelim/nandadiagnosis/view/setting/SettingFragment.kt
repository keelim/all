package com.keelim.nandadiagnosis.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.view.LoginActivity
import com.keelim.nandadiagnosis.view.WebViewActivity

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "nandaHome" -> {
                Intent(context, WebViewActivity::class.java).apply {
                    putExtra("URL", "https://keelim.github.io/nandaDiagnosis/")
                    startActivity(this)
                }
                true
            }
            "opensource" -> {
                startActivity(Intent(context, OpenSourceActivity::class.java))
                return true
            }
            "please" -> {
                startActivity(Intent(context, PleaseActivity::class.java))
                return true
            }
            "lab" -> {
                startActivity(Intent(context, LoginActivity::class.java))
                true
            }
            else -> false
        }
    }

}