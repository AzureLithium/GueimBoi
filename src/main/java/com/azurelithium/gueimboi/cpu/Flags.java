package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

class Flags {

    private int flags; // Layout => ZNHC---- : Z = Zero flag, N = Add/Sub flag, H = Half-carry
                        // flag, C = Carry flag
    private static final int Z_BIT = 7;
    private static final int N_BIT = 6;
    private static final int H_BIT = 5;
    private static final int C_BIT = 4;


    Flags() {
        flags = 0b00000000;
    }

    Flags(int _flags) {
        flags = _flags & 0xF0;
    }

    int getFlags() {
        return flags & 0xF0;
    }

    boolean isZ() {
        return ByteUtils.getBit(flags, Z_BIT);
    }

    boolean isN() {
        return ByteUtils.getBit(flags, N_BIT);
    }

    boolean isH() {
        return ByteUtils.getBit(flags, H_BIT);
    }

    boolean isC() {
        return ByteUtils.getBit(flags, C_BIT);
    }

    void setZ() {
        flags = ByteUtils.setBit(flags, Z_BIT);
    }

    void setN() {
        flags = ByteUtils.setBit(flags, N_BIT);
    }

    void setH() {
        flags = ByteUtils.setBit(flags, H_BIT);
    }

    void setC() {
        flags = ByteUtils.setBit(flags, C_BIT);
    }

    void resetZ() {
        flags = ByteUtils.resetBit(flags, Z_BIT);
    }

    void resetN() {
        flags = ByteUtils.resetBit(flags, N_BIT);
    }

    void resetH() {
        flags = ByteUtils.resetBit(flags, H_BIT);
    }

    void resetC() {
        flags = ByteUtils.resetBit(flags, C_BIT);
    }

    void setZ(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, Z_BIT);
        } else {
            flags = ByteUtils.resetBit(flags, Z_BIT);
        }
    }

    void setN(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, N_BIT);
        } else {
            flags = ByteUtils.resetBit(flags, N_BIT);
        }
    }

    void setH(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, H_BIT);
        } else {
            flags = ByteUtils.resetBit(flags, H_BIT);
        }
    }

    void setC(boolean bit) {
        if (bit) {
            flags = ByteUtils.setBit(flags, C_BIT);
        } else {
            flags = ByteUtils.resetBit(flags, C_BIT);
        }
    }

    public String toString() {
        return "Z:" + (isZ() ? "1" : "0") + " N:" + (isN() ? "1" : "0") + " H:"
                + (isH() ? "1" : "0") + " C:" + (isC() ? "1" : "0");
    }

}
