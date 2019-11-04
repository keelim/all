package com.keelim.nandadiagnosis.mainFragment.my;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.keelim.nandadiagnosis.R;

public class MyFragment extends PreferenceFragmentCompat { //todo 설정을 만지는 프래그 먼트 -> 나중에 진짜로 활용 잘 할 수 있으면 좋을 듯

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);
        //셋팅 프레그먼트를 작성을 한다. -> 어떤 기능을 넣어야 할지는 아직 확정하지는 않았다.
    }
}
