package com.keelim.nandadiagnosis.view.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
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
                val intent_web = Intent(context, WebViewActivity::class.java).apply {
                    putExtra("URL", "https://keelim.github.io/nandaDiagnosis/")
                    startActivity(this)
                }
                true
            }
            "question" -> {
                changeFragment(QuestionFragment())
                        .commit()
                return true
            }
            "opensource" -> {
                changeFragment(OpenSourceFragment())
                        .commit()
                return true
            }
            "please" -> {
                changeFragment(PleaseFragment())
                        .commit()
                return true
            }
            "lab" -> {
                startActivity(Intent(context, LoginActivity::class.java))
                true
            }
            else -> false
        }
    }

    private fun changeFragment(fragment: Fragment): FragmentTransaction {
        return activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
    }
}