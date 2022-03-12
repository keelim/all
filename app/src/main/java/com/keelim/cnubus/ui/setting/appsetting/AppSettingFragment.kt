package com.keelim.cnubus.ui.setting.appsetting

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.setting.LabActivity
import com.keelim.common.extensions.toast
import com.keelim.labs.ui.capture.CaptureActivity


class AppSettingFragment : PreferenceFragmentCompat() {
    private val appPermissions: Array<String> by lazy {
        val data = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            data.plus(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        data
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter { appPermissions.contains(it.key) }
            if (responsePermissions.filter { it.value }.size == appPermissions.size) {
                toast("권한이 확인되었습니다.")
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_setting)
        initViews()
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        preference ?: return false
        when (preference.key) {
            "앱설정" -> {
                startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:com.keelim.cnubus")
                })
            }
            "문의" -> sendEmailToAdmin()
            "권한" -> permissionLauncher.launch(appPermissions)
            "오픈소스" -> startActivity(
                Intent(
                    requireContext(),
                    OssLicensesMenuActivity::class.java
                )
            )
            "lab1" -> startActivity(
                Intent(
                    requireContext(),
                    LabActivity::class.java
                )
            )
            "lab2" -> startActivity(
                Intent(
                    requireContext(),
                    CaptureActivity::class.java
                )
            )
        }
        return true
    }

    private fun initViews() {
        val info = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val preference = findPreference<Preference>("버전")
        preference?.summary = info.versionName
    }

    private fun sendEmailToAdmin(
        title: String = "CNUBUS에 대한 문의",
        receivers: Array<String> = arrayOf("kimh00335@gmail.com"),
    ) {
        requireContext().startActivity(Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_SUBJECT, title)
            putExtra(Intent.EXTRA_EMAIL, receivers)
            putExtra(Intent.EXTRA_TEXT,
                String.format("App Version : %s\nDevice : %s\nAndroid(SDK) : %d(%s)\n내용 : ",
                    BuildConfig.VERSION_NAME,
                    Build.MODEL,
                    Build.VERSION.SDK_INT,
                    Build.VERSION.RELEASE))
            type = "message/rfc822"
        })
    }
}
