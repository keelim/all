package com.keelim.cnubus.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.keelim.cnubus.R
import com.keelim.cnubus.model.ModeCode
import kotlinx.android.synthetic.main.activity_settings.*

class SettingActivity : AppCompatActivity(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(
            R.id.settings,
            SettingsFragment()
        ).commit()

        supportActionBar!!.setDisplayShowHomeEnabled(true)
        Snackbar.make(setting_container, "설정화면 입니다. ", Snackbar.LENGTH_LONG).show()
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
                "app_share" -> {
                    Toast.makeText(activity, "기능 준비 중 입니다. ", Toast.LENGTH_SHORT).show()
                    return true
                }

                "opensource" -> {
                    Intent(context, OpenSourceActivity::class.java).apply {
                        startActivity(this)
                    }
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

                "lab1" -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("실험기능")
                        .setMessage("실험기능으로 앱이 갑자기 정지 할 수 있습니다.\n 그래도 실행하시겠습니까")
                        .setPositiveButton(
                            "예"
                        ) { _: DialogInterface?, _: Int ->
                            val lab = Intent(context, MapsActivity::class.java)
                            lab.putExtra("lab1", ModeCode.LAB1.ordinal)
                            startActivity(lab)
                        }
                        .setNegativeButton(
                            "아니오"
                        ) { _: DialogInterface?, _: Int -> }
                        .show()
                    return true
                }
            }
            return false
        }

        private fun getVersionInfo(): String? { // 버전을 확인을 하는 메소드
            return requireActivity().packageManager.getPackageInfo(
                requireContext().packageName,
                0
            ).versionName
        }
    }
}