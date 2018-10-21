package com.azurelithium.gueimboi.memory;

public class MMU {

    private RAM ram;

    public MMU() {
        ram = new RAM();
    }

    int readByte(int address) {
        return ram.readByte(address);
    }

    void writeByte(int address, byte value) {
        ram.writeByte(address, value);
    }

    int[] readBytes(int address, int count) {
        return ram.readBytes(address, count);
    }

    void writeBytes(int address, int[] bytes) {
        ram.writeBytes(address, bytes);
    }

}
