package com.azurelithium.gueimboi.utils;

public final class StringUtils {

    public static String toHex(int number) {
        return "0x" + Integer.toString(number, 16).toUpperCase();
    }

    public static String toHex(int[] numbers) {
        final StringBuilder builder = new StringBuilder();
        for (int number : numbers) {
            builder.append(Integer.toString(number, 16));
        }
        return "0x" + builder.toString().toUpperCase();
    }

}
