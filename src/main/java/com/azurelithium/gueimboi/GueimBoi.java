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
    static CPU CPU;
    static MMU MMU;

    /**
     * Emulator entry point.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {  
        initialize();
        start();
    }

    private static void initialize() {
        logger.info("Initializing GueimBoi components.");
        MMU = new MMU();
        CPU = new CPU(MMU);
    }

    private static void start() {
        logger.info("Starting GueimBoi."); 
        while (true) {
            CPU.run();
        }
    }
}
