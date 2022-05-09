package com.penelope.seatforyou.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;

public class TimeUtils {

    public static int VIBE_SHORT = 4;

    public static LocalDate toDate(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime toDateTime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String formatTime(int openHour, int openMinute, int closeHour, int closeMinute) {
        return String.format(Locale.getDefault(), "%s - %s",
                formatTime(openHour, openMinute),
                formatTime(closeHour, closeMinute)
        );
    }

    public static String formatTime(int hour, int minute) {
        return String.format(Locale.getDefault(), "%d:%02d %s",
                hour == 12 ? 12 : hour % 12,
                minute,
                hour < 12 ? "AM" : "PM"
        );
    }

    public static String formatDate(int year, int month, int dayOfMonth) {
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, dayOfMonth);
    }

    public static String formatDateTime(int month, int dayOfMonth, int hour, int minute) {
        return String.format(Locale.getDefault(), "%d월 %d일 %s",
                month, dayOfMonth, formatTime(hour, minute));
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return String.format(Locale.getDefault(), "%d월 %d일 %s",
                dateTime.getMonthValue(), dateTime.getDayOfMonth(),
                formatTime(dateTime.getHour(), dateTime.getMinute())
        );
    }

}
