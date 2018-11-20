package com.azurelithium.gueimboi.memory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if (address == 0xFF50 && value == 0x01) { // bootrom disabling
            loadCartridge();
        }
    }

    public int getMemRegister(MemRegister memRegister) {
        int memRegisterAddress = MemRegisterAddresses.get(memRegister);
        return readByte(memRegisterAddress);
    }

    public void setMemRegister(MemRegister memRegister, int value) {
        int memRegisterAddress = MemRegisterAddresses.get(memRegister);
        writeByte(memRegisterAddress, value);
    }

    public void loadCartridge() {
        try {
            byte[] bFile = Files
                .readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\01-special.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\02-interrupts.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\03-op sp,hl.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\04-op r,imm.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\05-op rp.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\06-ld r,r.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\07-jr,jp,call,ret,rst.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\08-misc instrs.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\09-op r,r.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\10-bit ops.gb"));
                //.readAllBytes(Paths.get(".\\src\\test\\resources\\blargg's_test_roms\\cpu_instrs\\11-op a,(hl).gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/01-special.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/02-interrupts.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/03-op sp,hl.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/04-op r,imm.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/05-op rp.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/06-ld r,r.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/07-jr,jp,call,ret,rst.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/08-misc instrs.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/09-op r,r.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/10-bit ops.gb"));
                //.readAllBytes(Paths.get("./src/test/resources/blargg's_test_roms/cpu_instrs/11-op a,(hl).gb"));
            ram.writeBytes(0x0000, convertToIntArray(bFile));
        } catch (IOException e) {
        } ;
    }

    public static int[] convertToIntArray(byte[] input) {
        int[] ret = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            ret[i] = input[i] & 0xff; // Range 0 to 255, not -128 to 127
        }
        return ret;
    }

}
