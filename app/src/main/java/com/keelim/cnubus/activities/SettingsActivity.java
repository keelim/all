package com.keelim.cnubus.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.keelim.cnubus.R;
import com.keelim.cnubus.activities.developer.DeveloperActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
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
            } else if (key.equals("opensource")) {
                Intent intent = new Intent(getContext(), OpenSourceActivity.class);
                startActivity(intent);
                return true;
            } else if (key.equals("update")) {
                Toast.makeText(getContext(), "업데이트 기능 준비 중입니다. 잠시만 기다려 주세요", Toast.LENGTH_SHORT).show();
            }

            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}