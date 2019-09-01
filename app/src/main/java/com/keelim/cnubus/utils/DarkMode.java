package com.keelim.cnubus.utils;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkMode { // 임의적으로 다크모드를 지정하는 클래스
    public void apply(boolean enabled) {
        int nightMode;
        if (enabled)
            nightMode = AppCompatDelegate.MODE_NIGHT_YES;
        else
            nightMode = AppCompatDelegate.MODE_NIGHT_NO;

        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    public boolean isEnabled(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }


}
