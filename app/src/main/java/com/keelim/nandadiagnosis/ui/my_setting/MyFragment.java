package com.keelim.nandadiagnosis.ui.my_setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.keelim.nandadiagnosis.R;

public class MyFragment extends PreferenceFragmentCompat { //todo 설정을 만지는 프래그 먼트 -> 나중에 진짜로 활용 잘 할 수 있으면 좋을 듯

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
    }
}
