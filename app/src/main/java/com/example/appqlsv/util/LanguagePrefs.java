package com.example.appqlsv.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class LanguagePrefs {
    private static final String LANG = "language";

    public static String getLang(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(LANG, "");
    }

    public static void setLang(Context context, String lang) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LANG, lang);
        editor.apply();
    }

    public static void removeLang(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(LANG);
        editor.apply();
    }
}
