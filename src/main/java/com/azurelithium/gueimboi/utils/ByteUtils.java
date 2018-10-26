package com.azurelithium.gueimboi.utils;

import static com.google.common.base.Preconditions.checkPositionIndex;

public final class ByteUtils {

    /**
     * Extraction
     */

    public static boolean getBit(int intValue, int position) {
        checkIntPosition("intValue", position);
        return (intValue & (1 << position)) != 0;
    }

    public static int getMSB(int value) {
        return (value >> 8) & 0xFF;
    }

    public static int getLSB(int value) {
        return value & 0xFF;
    }

    /**
     * Composition
     */

    public static int toWord(int MSB, int LSB) {
        return (MSB << 8 | LSB & 0xFF);
    }

    /**
     * Modification
     */

    public static int setBit(int value, int position) {
        checkIntPosition("intValue", position);
        return (((1 << position) | value) & 0xFFFFFFFF);
    }

    public static int unsetBit(int value, int position) {
        checkIntPosition("intValue", position);
        return (~(1 << position) & value & 0xFFFFFFFF);
    }

    /**
     * Validation
     */

    private static void checkIntPosition(String argumentName, int position) {
        checkPositionIndex(position, Integer.SIZE - 1,
                "Position " + position + "is inaccessible in " + argumentName);
    }

}
