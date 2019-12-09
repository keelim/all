package com.keelim.cnubus.mainfragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.keelim.cnubus.R;
import com.keelim.cnubus.activities.developer.DeveloperActivity;

class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) { //preferebce 클릭 리스너
        String key = preference.getKey();
        if (key.equals("developer")) {
            Intent intent_developer = new Intent(getContext(), DeveloperActivity.class);
            startActivity(intent_developer);
            return true;
        }
        return false;
    }
}
