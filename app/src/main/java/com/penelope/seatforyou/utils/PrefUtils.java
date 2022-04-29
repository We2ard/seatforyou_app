package com.penelope.seatforyou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {

    private static SharedPreferences getPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getCurrentUid(Context context) {
        return getPref(context).getString("uid", null);
    }

    public static void setCurrentUid(Context context, String uid) {
        getPref(context).edit().putString("uid", uid).apply();
    }

}
