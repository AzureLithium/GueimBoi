package com.azurelithium.gueimboi.memory;

class RAM {

    private static final int RAM_SIZE = 0x10000;
    private int[] ram;

    RAM() {
        ram = new int[RAM_SIZE];
    }

    int readByte(int address) {
        return ram[address];
    }

    void writeByte(int address, byte value) {
        ram[address] = value;
    }

    int[] readBytes(int address, int count) {
        int[] bytes = new int[count];
        System.arraycopy(ram, address, bytes, 0, bytes.length);
        return bytes;
    }

    void writeBytes(int address, int[] bytes) {
        System.arraycopy(bytes, 0, ram, address, bytes.length);
    }

}