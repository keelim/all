/*
 * Designed and developed by 2021 keelim (Jaehyun Kim)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keelim.cnubus.ui.setting.appsetting

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import com.keelim.common.extensions.toast

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
                startActivity(
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:com.keelim.cnubus")
                    }
                )
            }
            "문의" -> sendEmailToAdmin()
            "권한" -> permissionLauncher.launch(appPermissions)
            "오픈소스" -> startActivity(
                Intent(
                    requireContext(),
                    OssLicensesMenuActivity::class.java
                )
            )
            "lab" -> findNavController().navigate(R.id.open_lab_fragment)
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
        requireContext().startActivity(
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_EMAIL, receivers)
                putExtra(
                    Intent.EXTRA_TEXT,
                    String.format(
                        "App Version : %s\nDevice : %s\nAndroid(SDK) : %d(%s)\n내용 : ",
                        BuildConfig.VERSION_NAME,
                        Build.MODEL,
                        Build.VERSION.SDK_INT,
                        Build.VERSION.RELEASE
                    )
                )
                type = "message/rfc822"
            }
        )
    }
}
