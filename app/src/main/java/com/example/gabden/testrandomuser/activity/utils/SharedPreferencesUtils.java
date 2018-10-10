package com.example.gabden.testrandomuser.activity.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {

    private SharedPreferencesUtils() {}

    private static final String PREF_NAME = "randomusers_preferences";
    private static final String USERS_COUNT = "users_count";
    public static final String DEFAULT_USER_COUNT = "100";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static String getSavedUsersCount(Context context) {
        return getPreferences(context).getString(USERS_COUNT, DEFAULT_USER_COUNT);
    }

    public static void setSavedUsersCount(Context context, String count) {
        getEditor(context).putString(USERS_COUNT, count).apply();
    }
}
