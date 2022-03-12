package com.keelim.cnubus.ui.setting.appsetting

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import kotlinx.parcelize.Parcelize


class AppSettingFragment : PreferenceFragmentCompat() {

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
