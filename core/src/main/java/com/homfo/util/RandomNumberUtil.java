package com.homfo.util;

import java.util.Random;

public class RandomNumberUtil {
    public static String random(int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(9));
        }

        return result.toString();
    }
}
