package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;

/**
 * GueimBoi - GameBoy emulator
 */
public class GueimBoi {
    /**
     * Emulator entry point.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
        while (true) {
            cpu.runInstruction();
        }
    }
}
