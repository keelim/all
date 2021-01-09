package com.keelim.cnubus.ui.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.OpenSourceActivity
import com.keelim.cnubus.ui.content.ContentActivity

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        readyReview()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean { //preference 클릭 리스너
        when (preference.key) {
            "content" -> {
                requireActivity().startActivity(Intent(requireActivity(), ContentActivity::class.java))
            }

            "homepage" -> {
                requireActivity().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.notification_uri))))
            }

            "opensource" -> {
                startActivity(Intent(requireActivity(), OpenSourceActivity::class.java))
            }

            "update" -> {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(getString(R.string.updateLink))
                    startActivity(this)
                }
            }

            "dark" -> Toast.makeText(requireActivity(), "업데이트 준비 중 입니다", Toast.LENGTH_SHORT).show()
        }
        return super.onPreferenceTreeClick(preference)
    }

    private fun readyReview() {
        val manager = ReviewManagerFactory.create(requireActivity())

        manager.requestReviewFlow().apply {
            addOnCompleteListener {
                if (this.isSuccessful) {
                    manager.launchReviewFlow(requireActivity(), this.result)
                }
            }
        }
    }
}
