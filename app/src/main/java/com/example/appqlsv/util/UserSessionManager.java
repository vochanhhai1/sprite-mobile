package com.example.appqlsv.util;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSessionManager {
    private static final String PREFERENCE_NAME = "UserSession";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_FULLNAME = "fullname";
    private static final String KEY_REMEMBER_ME = "rememberMe";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    public UserSessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveFullname(String fullname)
    {
        editor.putString(KEY_FULLNAME, fullname);
        editor.apply();
    }
    public void saveCredentials(String username, String password) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.apply();
    }

    public void saveRememberMe(boolean rememberMe) {
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }
    public String getFullname() {
        return sharedPreferences.getString(KEY_FULLNAME, "");
    }
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public boolean getRememberMe() {
        return sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
    }
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1); // Trả về -1 nếu không tìm thấy userId
    }
    public void savePhoneNumber(String phoneNumber) {
        editor.putString(KEY_PHONE_NUMBER, phoneNumber);
        editor.apply();
    }
    public String getPhoneNumber() {
        return sharedPreferences.getString(KEY_PHONE_NUMBER, "");
    }

    public void clearCredentials() {
        editor.remove(KEY_USERNAME);
        editor.remove(KEY_PASSWORD);
        editor.remove(KEY_FULLNAME);
        editor.remove(KEY_PHONE_NUMBER);
        editor.apply();
    }
}
