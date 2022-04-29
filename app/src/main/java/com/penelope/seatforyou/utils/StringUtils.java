package com.penelope.seatforyou.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class StringUtils {

    public static String concat(List<String> list, String joint) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != list.size() - 1) {
                sb.append(joint);
            }
        }

        return sb.toString();
    }

    public static String phone(String text) {
        if (text.length() == 11) {
            return String.format(Locale.getDefault(), "%s-%s-%s",
                    text.substring(0, 3),
                    text.substring(3, 7),
                    text.substring(7, 11)
            );
        } else if (text.length() == 10) {
            return String.format(Locale.getDefault(), "%s-%s-%s",
                    text.substring(0, 3),
                    text.substring(3, 6),
                    text.substring(6, 10)
            );
        }
        return text;
    }

    public static String price(int p) {
        return String.format("%s원", NumberFormat.getInstance().format(p));
    }

}
