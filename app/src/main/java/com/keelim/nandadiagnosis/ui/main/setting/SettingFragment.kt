package com.keelim.nandadiagnosis.ui.main.setting


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import com.google.android.play.core.review.ReviewManagerFactory
import com.keelim.nandadiagnosis.R
import com.keelim.nandadiagnosis.ui.WebActivity
import com.keelim.nandadiagnosis.ui.kakao_search.SearchActivity
import com.keelim.nandadiagnosis.ui.open.OpenSourceActivity


class SettingFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preferences)
        readyReview()
    }

    override fun onPreferenceTreeClick(preference: androidx.preference.Preference?): Boolean {
        when (preference!!.key) {
            "blog" -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse((getString(R.string.blog_url)))))

            "home" -> Intent(context, WebActivity::class.java).apply {
                Toast.makeText(activity, "홈페이지 재구성 중 입니다.", Toast.LENGTH_SHORT).show()
            }

            "opensource" -> startActivity(Intent(context, OpenSourceActivity::class.java))

            "update" -> Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(getString(R.string.urinanda))
                startActivity(this)
            }

            "dark" -> {
                Toast.makeText(requireActivity(), "업데이트 준비 중 입니다", Toast.LENGTH_SHORT).show()
            }

            "search"->{
                requireActivity().startActivity(Intent(requireActivity(), SearchActivity::class.java))
            }
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