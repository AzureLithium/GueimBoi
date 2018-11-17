package com.azurelithium.gueimboi.memory;

import java.util.TreeMap;

public class MMU {

    public enum MemRegister {
        LCDC, STAT, SCY, SCX, LY, LYC, BGP
    }

    private TreeMap<MemRegister, Integer> MemRegisterAddresses =
            new TreeMap<MemRegister, Integer>() {
                {
                    put(MemRegister.LCDC, 0xFF40);
                    put(MemRegister.STAT, 0xFF41);
                    put(MemRegister.SCY, 0xFF42);
                    put(MemRegister.SCX, 0xFF43);
                    put(MemRegister.LY, 0xFF44);
                    put(MemRegister.LYC, 0xFF45);
                    put(MemRegister.BGP, 0xFF47);
                }
            };

    private RAM ram;

    public MMU() {
        ram = new RAM();
        ram.writeBytes(0x0000, ROM.GAMEBOY_ROM);
        ram.writeBytes(0x0104, ROM.NINTENDO_LOGO); // NINTENDO LOGO needed to pass the logo check =>
                                                   // debug purposes while cartridges are not
                                                   // implemented
        ram.writeByte(0x014D, 0xE7); // COMPLEMENT CHECK value needed to pass a jump => debug
                                     // purposes while cartridges are not implemented
    }

    public int readByte(int address) {
        return ram.readByte(address);
    }

    public void writeByte(int address, int value) {
        ram.writeByte(address, value);
    }

    public int getMemRegister(MemRegister memRegister) {
        int memRegisterAddress = MemRegisterAddresses.get(memRegister);
        return readByte(memRegisterAddress);
    }

    public void setMemRegister(MemRegister memRegister, int value) {
        int memRegisterAddress = MemRegisterAddresses.get(memRegister);
        writeByte(memRegisterAddress, value);
    }

}
