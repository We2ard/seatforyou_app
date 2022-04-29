package com.penelope.seatforyou.utils;

import java.util.Locale;

public class TimeUtils {
    public static final int VIBE_SHORT = 4; //ms

    public static String format(int openHour, int openMinute, int closeHour, int closeMinute) {
        return String.format(Locale.getDefault(), "%s - %s",
                format(openHour, openMinute),
                format(closeHour, closeMinute)
        );
    }

    public static String format(int hour, int minute) {
        return String.format(Locale.getDefault(), "%d:%02d %s",
                hour == 12 ? 12 : hour % 12,
                minute,
                hour < 12 ? "AM" : "PM"
        );
    }

}
