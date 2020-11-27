package com.keelim.cnubus.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R

class SettingActivity : AppCompatActivity(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.settings_preferences)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean { //preferebce 클릭 리스너
            when (preference.key) {

                "opensource" -> {
                    startActivity(Intent(activity, OpenSourceActivity::class.java))
                    return true
                }
                "update" -> {
                    Toast.makeText(context, "버전 확인 중입니다.", Toast.LENGTH_SHORT).show()
                    preference.summary = getVersionInfo()
                    return true
                }
                "mail" -> {
                    Intent(Intent.ACTION_SEND).apply {
                        this.type = "plain/Text"
                        this.putExtra(Intent.EXTRA_EMAIL, "kimh00335@gmail.com")
                        this.putExtra(Intent.EXTRA_SUBJECT, "[cnuBus] 문의 사항")
                        startActivity(this)
                    }
                    return true
                }
            }
            return false
        }

        private fun getVersionInfo(): String { // 버전을 확인을 하는 메소드
            return BuildConfig.VERSION_NAME
        }
    }
}