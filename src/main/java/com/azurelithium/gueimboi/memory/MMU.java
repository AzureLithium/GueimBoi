package com.azurelithium.gueimboi.memory;

import org.apache.commons.lang3.ArrayUtils;

public class MMU {

    private RAM ram;
    private static final boolean LITTLE_ENDIAN = true;

    public MMU() {
        ram = new RAM();
        ram.writeBytes(0x0000, ROM.GAMEBOY_ROM);
    }

    public int readByte(int address) {
        return ram.readByte(address);
    }

    public void writeByte(int address, int value) {
        ram.writeByte(address, value);
    }

    public int[] readBytes(int address, int count) {
        int[] bytes = ram.readBytes(address, count);
        if (LITTLE_ENDIAN) {
            ArrayUtils.reverse(bytes);
        }
        return bytes;
    }

    public void writeBytes(int address, int[] bytes) {
        if (LITTLE_ENDIAN) {
            ArrayUtils.reverse(bytes);
        }
        ram.writeBytes(address, bytes);
    }

}
