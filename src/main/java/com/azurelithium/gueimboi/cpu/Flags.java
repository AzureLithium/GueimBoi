package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

class Flags {

    private int flags; // Layout => ZNHC---- : Z = Zero flag, N = Add/Sub flag, H = Half-carry
                        // flag, C = Carry flag
    private static final int Z_bit = 7;
    private static final int N_bit = 6;
    private static final int H_bit = 5;
    private static final int C_bit = 4;


    Flags() {
        flags = 0b00000000;
    }

    Flags(int _flags) {
        flags = _flags & 0xF0;
    }

    int getFlags() {
        return flags;
    }

    boolean isZ() {
        return ByteUtils.getBit(flags, Z_bit);
    }

    boolean isN() {
        return ByteUtils.getBit(flags, N_bit);
    }

    boolean isH() {
        return ByteUtils.getBit(flags, H_bit);
    }

    boolean isC() {
        return ByteUtils.getBit(flags, C_bit);
    }

    void setZ() {
        flags = ByteUtils.setBit(flags, Z_bit);
    }

    void setN() {
        flags = ByteUtils.setBit(flags, N_bit);
    }

    void setH() {
        flags = ByteUtils.setBit(flags, H_bit);
    }

    void setC() {
        flags = ByteUtils.setBit(flags, C_bit);
    }

    void resetZ() {
        flags = ByteUtils.resetBit(flags, Z_bit);
    }

    void resetN() {
        flags = ByteUtils.resetBit(flags, N_bit);
    }

    void resetH() {
        flags = ByteUtils.resetBit(flags, H_bit);
    }

    void resetC() {
        flags = ByteUtils.resetBit(flags, C_bit);
    }

    void setZ(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, Z_bit);
        } else {
            flags = ByteUtils.resetBit(flags, Z_bit);
        }
    }

    void setN(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, N_bit);
        } else {
            flags = ByteUtils.resetBit(flags, N_bit);
        }
    }

    void setH(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, H_bit);
        } else {
            flags = ByteUtils.resetBit(flags, H_bit);
        }
    }

    void setC(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, C_bit);
        } else {
            flags = ByteUtils.resetBit(flags, C_bit);
        }
    }

    public String toString() {
        return "Z:" + (isZ() ? "1" : "0") + " N:" + (isN() ? "1" : "0") + " H:"
                + (isH() ? "1" : "0") + " C:" + (isC() ? "1" : "0");
    }

}
