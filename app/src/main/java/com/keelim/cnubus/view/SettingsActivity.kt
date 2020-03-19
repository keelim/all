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

class SettingsActivity : AppCompatActivity(R.layout.activity_settings) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.settings,
            SettingsFragment()
        ).commit()

        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        Snackbar.make(setting_container, "설정화면 입니다. ",Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(
            savedInstanceState: Bundle?,
            rootKey: String?
        ) {
            addPreferencesFromResource(R.xml.settings_preferences)
        }

        override fun onPreferenceTreeClick(preference: Preference): Boolean { //preferebce 클릭 리스너
            when (preference.key) {
                "app_share" -> return true

                "opensource" -> {
                    val intent = Intent(context, OpenSourceActivity::class.java)
                    startActivity(intent)
                    return true
                }
                "update" -> {
                    Toast.makeText(context, "버전 확인 중입니다.", Toast.LENGTH_SHORT).show()
                    preference.summary = getVersionInfo(context!!)
                    return true
                }
                "mail" -> {
                    val mail = Intent(Intent.ACTION_SEND)
                    mail.type = "plain/Text"
                    mail.putExtra(Intent.EXTRA_EMAIL, "kimh00335@gmail.com")
                    mail.putExtra(Intent.EXTRA_SUBJECT, "[cnuBus] 문의 사항")
                    startActivity(mail)
                    return true
                }
                "lab1" -> {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("실험기능")
                        .setMessage("실험기능으로 앱이 갑자기 정지 할 수 있습니다.\n 그래도 실행하시겠습니까")
                        .setPositiveButton(
                            "예"
                        ) { dialog: DialogInterface?, which: Int ->
                            val lab = Intent(context, MapsActivity::class.java)
                            lab.putExtra("lab1", ModeCode.LAB1.ordinal)
                            startActivity(lab)
                        }
                        .setNegativeButton(
                            "아니오"
                        ) { dialog: DialogInterface?, which: Int -> }
                        .show()
                    return true
                }
                "lab2" -> {
                    MaterialAlertDialogBuilder(context)
                        .setTitle("실험기능")
                        .setMessage("실험기능으로 앱이 갑자기 정지 할 수 있습니다.\n 그래도 실행하시겠습니까")
                        .setPositiveButton("예") { dialog: DialogInterface?, which: Int ->
                            val lab2 = Intent(context, TempActivity::class.java)
                            startActivity(lab2)
                        }
                        .setNegativeButton(
                            "아니오"
                        ) { _: DialogInterface?, _: Int -> }
                        .show()
                }
            }
            return false
        }

        private fun getVersionInfo(context: Context): String? { // 버전을 확인을 하는 메소드
            return context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }
    }
}