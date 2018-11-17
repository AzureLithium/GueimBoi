package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.gpu.GPU;
import com.azurelithium.gueimboi.gui.Display;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GueimBoi - GameBoy emulator
 */
public class GueimBoi {

    final static Logger logger = LoggerFactory.getLogger(GueimBoi.class);
    
    final static int GAMEBOY_CYCLE_RATE = 4194304;
    final static int CYCLES_PER_FRAME = 70224;
    static float delta;

    static CPU CPU;
    static MMU MMU;
    static GPU GPU;
    static Display display;
    
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
        double frameRate = (double)GAMEBOY_CYCLE_RATE / CYCLES_PER_FRAME; // 59.727501 hz
        display = new Display(frameRate);
        GPU = new GPU(display, MMU);
    }

    private static void start() {
        logger.info("Starting GueimBoi.");
        run();
    }

    private static void run() {
        int CPUtickFreq = 4;
        display.initializeFrameTime();
        while(true) {
            int cycles = 0;
            while (cycles < CYCLES_PER_FRAME) {
                if (cycles % CPUtickFreq == 3) { // first tick as soon as posible => % 0, delayed first tick => % 3
                    CPU.tick();    
                }
                GPU.tick();
                cycles++;
            }
            display.waitRefresh();
        }
    }
}
