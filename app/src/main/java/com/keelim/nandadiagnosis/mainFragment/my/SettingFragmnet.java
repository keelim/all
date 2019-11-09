package com.keelim.nandadiagnosis.mainFragment.my;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.keelim.nandadiagnosis.R;

public class SettingFragmnet extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }

}
