package com.keelim.cnubus.utils;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkMode { //dark mode setting
    public void apply(boolean enabled) {
        int nightMode;
        if (enabled)
            nightMode = AppCompatDelegate.MODE_NIGHT_YES;
        else
            nightMode = AppCompatDelegate.MODE_NIGHT_NO;

        AppCompatDelegate.setDefaultNightMode(nightMode);
    }
}
