package com.example.appqlsv.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

public class NightModePrefs {

    private static final String NIGHT_MODE_KEY = "night_mode";
    private static final String SHARED_PREFS_NAME = "MyPrefs";

    private final SharedPreferences sharedPreferences;

    public NightModePrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setNightModeEnabled(boolean nightModeEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(NIGHT_MODE_KEY, nightModeEnabled);
        editor.apply();
    }

    public boolean isNightModeEnabled() {
        return sharedPreferences.getBoolean(NIGHT_MODE_KEY, false);
    }

    public boolean isSystemNightModeEnabled(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

}
