package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;

public class Flags {

    private byte flags; // Layout => ZNHC---- : Z = Zero flag, N = Add/Sub flag, H = Half-carry
                        // flag, C = Carry flag
    private static final int Z_bit = 7;
    private static final int N_bit = 6;
    private static final int H_bit = 5;
    private static final int C_bit = 4;


    public Flags() {
        flags = 0b00000000;
    }

    public Flags(byte _flags) {
        flags = _flags;
    }

    byte getFlags() {
        return flags;
    }

    public boolean isZ() {
        return ByteUtils.getBit(flags, Z_bit);
    }

    public boolean isN() {
        return ByteUtils.getBit(flags, N_bit);
    }

    public boolean isH() {
        return ByteUtils.getBit(flags, H_bit);
    }

    public boolean isC() {
        return ByteUtils.getBit(flags, C_bit);
    }

    public void setZ() {
        ByteUtils.setBit(flags, Z_bit);
    }

    public void setN() {
        ByteUtils.setBit(flags, N_bit);
    }

    public void setH() {
        ByteUtils.setBit(flags, H_bit);
    }

    public void setC() {
        ByteUtils.setBit(flags, C_bit);
    }

    public void unsetZ() {
        ByteUtils.unsetBit(flags, Z_bit);
    }

    public void unsetN() {
        ByteUtils.unsetBit(flags, N_bit);
    }

    public void unsetH() {
        ByteUtils.unsetBit(flags, H_bit);
    }

    public void unsetC() {
        ByteUtils.unsetBit(flags, C_bit);
    }

    public void setZ(boolean bit) {
        if (bit) {
            ByteUtils.setBit(flags, Z_bit);
        } else {
            ByteUtils.unsetBit(flags, Z_bit);
        }
    }

    public void setN(boolean bit) {
        if (bit) {
            ByteUtils.setBit(flags, N_bit);
        } else {
            ByteUtils.unsetBit(flags, N_bit);
        }
    }

    public void setH(boolean bit) {
        if (bit) {
            ByteUtils.setBit(flags, H_bit);
        } else {
            ByteUtils.unsetBit(flags, H_bit);
        }
    }

    public void setC(boolean bit) {
        if (bit) {
            ByteUtils.setBit(flags, C_bit);
        } else {
            ByteUtils.unsetBit(flags, C_bit);
        }
    }

}
