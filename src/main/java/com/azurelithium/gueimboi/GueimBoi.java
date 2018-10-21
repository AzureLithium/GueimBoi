package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GueimBoi - GameBoy emulator
 */
public class GueimBoi {

    final static Logger logger = LoggerFactory.getLogger(GueimBoi.class);

    /**
     * Emulator entry point.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        logger.info("Starting GueimBoi.");
        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
        while (true) {
            cpu.runInstruction();
        }
    }
}
