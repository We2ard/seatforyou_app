package com.penelope.seatforyou.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtils {

    public static List<String> pick(List<String> items, int desiredNum) {

        int num = Math.min(desiredNum, items.size());

        List<String> picked = new ArrayList<>();

        while (picked.size() < num) {
            String randomItem = items.get(new Random().nextInt(items.size()));
            if (!picked.contains(randomItem)) {
                picked.add(randomItem);
            }
        }

        return picked;
    }

}
