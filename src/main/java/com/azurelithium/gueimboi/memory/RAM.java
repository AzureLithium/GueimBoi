package com.azurelithium.gueimboi.memory;

class RAM {

    private static final int RAM_SIZE = 0x10000;
    private int[] RAM;

    RAM() {
        RAM = new int[RAM_SIZE];
    }

    int readByte(int address) {
        return RAM[address];
    }

    void writeByte(int address, int value) {
        RAM[address] = value;
    }

    int[] readBytes(int address, int count) {
        int[] bytes = new int[count];
        System.arraycopy(RAM, address, bytes, 0, bytes.length);
        return bytes;
    }

    void writeBytes(int address, int[] bytes) {
        System.arraycopy(bytes, 0, RAM, address, bytes.length);
    }

}
