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
        if (key.equals("nandaHome")) {
            Intent intent_web = new Intent(getContext(), WebViewActivity.class);
            startActivity(intent_web);
            return true;
        } else if (key.equals("question")) {
            Intent intent_question = new Intent(getContext(), QuestionActivity.class);
            startActivity(intent_question);
            return true;
        } else if (key.equals("opensource")) {
            Intent intent_opensource = new Intent(getContext(), OpenSourceActivity.class);
            startActivity(intent_opensource);
            return true;
        } else if (key.equals("please")) {
            Intent intent_please = new Intent(getContext(), PleaseActivity.class);
            startActivity(intent_please);
            return true;
        } else if (key.equals("developer_page")) {
            Intent intent_developer = new Intent(getContext(), DeveloperActivity.class);
            intent_developer.putExtra("URL", "https://keelim.github.io/nandaDiagnosis/");
            startActivity(intent_developer);
            return true;
        } else {
            return false;
        }
    }
}
