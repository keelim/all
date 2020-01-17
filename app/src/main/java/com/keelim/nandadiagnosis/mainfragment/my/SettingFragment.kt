package com.keelim.nandadiagnosis.mainfragment.my

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.activities.WebViewActivity
import com.keelim.nandadiagnosis.help.OpenSourceActivity
import com.keelim.nandadiagnosis.help.PleaseActivity
import com.keelim.nandadiagnosis.help.QuestionActivity

class SettingFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            "nandaHome" -> {
                val intent_web = Intent(context, WebViewActivity::class.java)
                intent_web.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/")
                startActivity(intent_web)
                true
            }
            "question" -> {
                val intent_question = Intent(context, QuestionActivity::class.java)
                startActivity(intent_question)
                true
            }
            "opensource" -> {
                val intent_opensource = Intent(context, OpenSourceActivity::class.java)
                startActivity(intent_opensource)
                true
            }
            "please" -> {
                val intent_please = Intent(context, PleaseActivity::class.java)
                startActivity(intent_please)
                true
            }
            else -> false
        }
    }
}