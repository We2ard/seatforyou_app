package com.penelope.seatforyou.utils;

public class ImageUtils {

    public static final String URL_SHOP_FORMAT = "https://firebasestorage.googleapis.com/v0/b/seat-for-you.appspot.com/o/shops%2F{UID}.jpg?alt=media";

    public static String getShopImageUrl(String uid) {
        return URL_SHOP_FORMAT.replace("{UID}", uid);
    }

}
