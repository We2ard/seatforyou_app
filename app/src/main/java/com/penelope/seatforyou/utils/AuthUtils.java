package com.penelope.seatforyou.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AuthUtils {

    public static String emailize(String phone) {
        return phone + "@seatforu.com";
    }

}
