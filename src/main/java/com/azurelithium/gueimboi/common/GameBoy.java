package com.azurelithium.gueimboi.common;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.timer.Timer;
import com.azurelithium.gueimboi.gpu.GPU;
import com.azurelithium.gueimboi.gui.Display;
import com.azurelithium.gueimboi.joypad.InputController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameBoy {

    final Logger logger = LoggerFactory.getLogger(getClass());
    
    public final static int GAMEBOY_CYCLE_RATE = 4194304;
    public final static int CYCLES_PER_FRAME = 70224;
    public final static int CYCLES_PER_CPU_TICK = 4;

    String ROMPath;
    Display display;
    Thread gameboyThread;

    CPU CPU;
    MMU MMU;
    GPU GPU;    
    InputController joypad;
    Timer timer;

    public GameBoy(Display _display, InputController _joypad, String _ROMPath) {
        display = _display;
        joypad = _joypad;
        ROMPath = _ROMPath;
    }

    public void initialize() {
        logger.info("Initializing GueimBoi components.");
        MMU = new MMU(ROMPath);
        CPU = new CPU(MMU);        
        GPU = new GPU(display, MMU);
        timer = new Timer(MMU);
        MMU.setTimer(timer); 
        MMU.setInputController(joypad);
        joypad.setMMU(MMU);
        MMU.initializeMemRegisters();
    }

    public void start() {
        logger.info("Starting GueimBoi.");
        gameboyThread = new Thread() {
            public void run() {
                runGameboy();
            }
        };
        gameboyThread.start();
    }

    public void stop() {
        logger.info("Stopping GueimBoi.");
        gameboyThread.interrupt();
        gameboyThread = null;
    }

    void runGameboy() {
        display.initializeFrameTime();
        while(!Thread.currentThread().isInterrupted()) {
            int cycles = 0;
            while (cycles < CYCLES_PER_FRAME) {
                if (cycles % CYCLES_PER_CPU_TICK == 0) { // first tick as soon as posible => % 0, delayed first tick => % 3
                    CPU.tick();    
                }       
                GPU.tick();
                timer.tick();   
                cycles++;
            }
            display.waitRefresh();
        }
    }

    public StringBuilder getSerialContent() {
        return MMU.getSerialContent();
    }

}
