package com.keelim.nandadiagnosis.mainFragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.help.activity.OpenSourceActivity;
import com.keelim.nandadiagnosis.help.activity.QuestionActivity;

public class SettingFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
