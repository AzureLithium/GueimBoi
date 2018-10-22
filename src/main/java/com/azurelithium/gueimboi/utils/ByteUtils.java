package com.azurelithium.gueimboi.utils;

import static com.google.common.base.Preconditions.checkPositionIndex;

public class ByteUtils {

    /**
     * Calculation
     */

    public static boolean isNegative(int value) {
        return value >> Integer.SIZE - 1 != 0;
    }

    public static boolean isPositive(int value) {
        return !isNegative(value);
    }

    public static int abs(byte byteValue) {
        return isNegative(byteValue) ? 0x100 - byteValue & 0xFF : byteValue;
    }

    public static int abs(short wordValue) {
        return isNegative(wordValue) ? 0x10000 - wordValue & 0xFFFF : wordValue;
    }

    /**
     * Extraction
     */

    public static boolean getBit(int intValue, int position) {
        checkIntPosition("intValue", position);
        return (intValue & (1 << position)) != 0;
    }

    public static boolean getMSb(int value) {
        return isNegative(value);
    }

    public static boolean getLSb(int value) {
        return (value & 0x1) != 0;
    }

    public static int getMSB(short value) {
        return (byte) (value >> 8);
    }

    public static int getMSB(int value) {
        return (byte) (value >> 24);
    }

    public static int getLSB(int value) {
        return (byte) value;
    }

    public static int getMSW(int value) {
        return (short) (value >> 16);
    }

    public static int getLSW(int value) {
        return (short) value;
    }

    public static int toByte(int value) {
        return (byte) value;
    }

    public static int toWord(int value) {
        return (short) value;
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

    public static int setBit(byte byteValue, int position) {
        checkBytePosition("byteValue", position);
        return (int) (((1 << position) | byteValue) & 0xFF);
    }

    public static int setBit(short wordValue, int position) {
        checkWordPosition("wordValue", position);
        return (int) (((1 << position) | wordValue) & 0xFFFF);
    }

    public static int unsetBit(byte byteValue, int position) {
        checkBytePosition("byteValue", position);
        return (int) (~(1 << position) & byteValue & 0xFF);
    }

    public static int unsetBit(short wordValue, int position) {
        checkWordPosition("wordValue", position);
        return (int) (~(1 << position) & wordValue & 0xFFFF);
    }

    /**
     * Validation
     */

    private static void checkBytePosition(String argumentName, int position) {
        checkPositionIndex(position, Byte.SIZE - 1,
                "Position " + position + "is inaccessible in " + argumentName);
    }

    private static void checkWordPosition(String argumentName, int position) {
        checkPositionIndex(position, Short.SIZE - 1,
                "Position " + position + "is inaccessible in " + argumentName);
    }

    private static void checkIntPosition(String argumentName, int position) {
        checkPositionIndex(position, Integer.SIZE - 1,
                "Position " + position + "is inaccessible in " + argumentName);
    }

}
