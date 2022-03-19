package com.keelim.cnubus.ui.setting.appsetting

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.keelim.cnubus.R
import com.keelim.cnubus.ui.compose.Lab3Activity
import com.keelim.cnubus.ui.setting.LabActivity
import com.keelim.labs.ui.capture.CaptureActivity

class LabFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_lab)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {
        preference ?: return false
        when (preference.key) {
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
            "lab3" -> startActivity(
                Intent(
                    requireContext(),
                    Lab3Activity::class.java
                )
            )
        }
        return true
    }
}