package com.azurelithium.gueimboi.memory;

class Memory {

    private static final int MEM_SIZE = 0x10000;
    private int[] memory;

    Memory() {
        memory = new int[MEM_SIZE];
    }

    int readByte(int address) {
        return memory[address];
    }

    void writeByte(int address, int value) {
        memory[address] = value;
    }

    int[] readBytes(int address, int count) {
        int[] bytes = new int[count];
        System.arraycopy(memory, address, bytes, 0, bytes.length);
        return bytes;
    }

    void writeBytes(int address, int[] bytes) {
        System.arraycopy(bytes, 0, memory, address, bytes.length);
    }

}
