package com.keelim.cnubus.ui.main.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.cnubus.BuildConfig
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.OpenSourceActivity
import com.keelim.cnubus.ui.content.ContentActivity

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean { //preference 클릭 리스너
        when (preference.key) {
            "content" -> {
                requireActivity().startActivity(
                    Intent(
                        requireActivity(),
                        ContentActivity::class.java
                    )
                )
            }

            "homepage" -> {
                requireActivity().startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(getString(R.string.notification_uri))
                    )
                )
            }

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

            "review" -> {
                val manager = ReviewManagerFactory.create(requireContext())

                manager.requestReviewFlow().apply {
                    addOnCompleteListener {
                        if (this.isSuccessful) {
                            manager.launchReviewFlow(requireActivity(), this.result)
                        }
                    }
                }
            }
        }
        return false
    }

    private fun getVersionInfo(): String { // 버전을 확인을 하는 메소드
        return BuildConfig.VERSION_NAME
    }
}
