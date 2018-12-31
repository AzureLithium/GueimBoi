package com.azurelithium.gueimboi.utils;

public final class ByteUtils {

    /**
     * Extraction
     */

    public static boolean getBit(int intValue, int position) {
        checkIntPosition("intValue", position);
        return (intValue & (1 << position)) != 0;
    }

    public static int getMSB(int value) {
        return (value >> Byte.SIZE) & 0xFF;
    }

    public static int getLSB(int value) {
        return value & 0xFF;
    }

    /**
     * Composition
     */

    public static int toWord(int MSB, int LSB) {
        return ((MSB & 0xFF) << Byte.SIZE | LSB & 0xFF);
    }

    /**
     * Modification
     */

    public static int setBit(int value, int position) {
        checkIntPosition("intValue", position);
        return (1 << position | value & 0xFFFF);
    }

    public static int resetBit(int value, int position) {
        checkIntPosition("intValue", position);
        return (~(1 << position) & value & 0xFFFF);
    }

    /**
     * Validation
     */

    private static void checkIntPosition(String argumentName, int position) {
        if (position < 0 || position >= Short.SIZE) {
            throw new IndexOutOfBoundsException("Position " + position + "is inaccessible in " + argumentName);
        }
    }

}
