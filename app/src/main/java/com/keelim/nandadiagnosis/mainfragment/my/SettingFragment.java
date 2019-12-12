package com.keelim.nandadiagnosis.mainfragment.my;

import android.content.Intent;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.DeveloperActivity;
import com.keelim.nandadiagnosis.activities.WebViewActivity;
import com.keelim.nandadiagnosis.help.OpenSourceActivity;
import com.keelim.nandadiagnosis.help.PleaseActivity;
import com.keelim.nandadiagnosis.help.QuestionActivity;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "nandaHome":
                Intent intent_web = new Intent(getContext(), WebViewActivity.class);
                intent_web.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/");
                startActivity(intent_web);
                return true;
            case "question":
                Intent intent_question = new Intent(getContext(), QuestionActivity.class);
                startActivity(intent_question);
                return true;
            case "opensource":
                Intent intent_opensource = new Intent(getContext(), OpenSourceActivity.class);
                startActivity(intent_opensource);
                return true;
            case "please":
                Intent intent_please = new Intent(getContext(), PleaseActivity.class);
                startActivity(intent_please);
                return true;
            case "developer_page":
                Intent intent_developer = new Intent(getContext(), DeveloperActivity.class);
                startActivity(intent_developer);
                return true;
            default:
                return false;
        }
    }
}
