package com.keelim.nandadiagnosis.mainFragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.keelim.nandadiagnosis.R;
import com.keelim.nandadiagnosis.activities.DeveloperActivity;
import com.keelim.nandadiagnosis.activities.WebViewActivity;
import com.keelim.nandadiagnosis.help.activity.OpenSourceActivity;
import com.keelim.nandadiagnosis.help.activity.PleaseActivity;
import com.keelim.nandadiagnosis.help.activity.QuestionActivity;

public class SettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_preferences);

    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        String key = preference.getKey();
        if(key.equals("nandaHome")){
            Intent intent_web = new Intent(getContext(), WebViewActivity.class);
            startActivity(intent_web);
            return true;
        } else if(key.equals("question")){
            Intent intent_question = new Intent(getContext(), QuestionActivity.class);
            startActivity(intent_question);
            return true;
        } else if(key.equals("opensource")){
            Intent intent_opensource = new Intent(getContext(), OpenSourceActivity.class);
            startActivity(intent_opensource);
            return true;
        } else if(key.equals("please")){
            Intent intent_please = new Intent(getContext(), PleaseActivity.class);
            startActivity(intent_please);
            return true;
        } else if(key.equals("developer_page")){
            Intent intent_developer = new Intent(getContext(), DeveloperActivity.class);
            startActivity(intent_developer);
            return true;
        } else {
            return false;
        }
    }
}
