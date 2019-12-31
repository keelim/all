package com.keelim.cnubus.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.keelim.cnubus.R;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings_preferences);
        }

        @Override
        public boolean onPreferenceTreeClick(@NotNull Preference preference) { //preferebce 클릭 리스너
            String key = preference.getKey();
            switch (key) {
                case "app_share":

                    break;
                case "opensource":
                    Intent intent = new Intent(getContext(), OpenSourceActivity.class);
                    startActivity(intent);
                    return true;

                case "update":
                    Toast.makeText(getContext(), "버전 확인 중입니다.", Toast.LENGTH_SHORT).show();
                    preference.setSummary(getVersionInfo(getContext()));
                    return true;

                case "mail":
                    Intent mail = new Intent(Intent.ACTION_SEND);
                    mail.setType("plain/Text");
                    mail.putExtra(Intent.EXTRA_EMAIL, "kimh00335@gmail.com");
                    mail.putExtra(Intent.EXTRA_SUBJECT, "[cnuBus] 문의 사항");
                    startActivity(mail);
                    return true;
            }
            return false;
        }

        private String getVersionInfo(@NotNull Context context) {
            String version = null;
            try {
                version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("update", "update error");
            }
            return version;
        }

    }

// private method

}