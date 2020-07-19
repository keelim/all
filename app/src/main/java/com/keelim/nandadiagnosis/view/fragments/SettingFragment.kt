package com.keelim.nandadiagnosis.view.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.view.OpenSourceActivity
import com.keelim.nandadiagnosis.view.PleaseActivity
import com.keelim.nandadiagnosis.view.WebActivity

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "blog" -> {
                /*Intent(context, WebViewActivity::class.java).apply {
                    putExtra("URL", "https://blog.naver.com/cjhdori")
                    startActivity(this)*/
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(("https://blog.naver.com/cjhdori"))))
                return true
            }

            "nandaHome" -> {
                Intent(context, WebActivity::class.java).apply {
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
                Toast.makeText(activity, "실험이 끝났습니다.", Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

}