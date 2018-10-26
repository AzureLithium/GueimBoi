package com.azurelithium.gueimboi.utils;

public final class StringUtils {

    public static String toHex(int number) {
        String result = Integer.toString(number, 16).toUpperCase();
        if (result.length() % 2 != 0) {
            result = "0" + result;
        }
        return "0x" + result;
    }

}
