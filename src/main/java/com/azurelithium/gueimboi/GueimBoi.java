package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.gui.MainWindow;

/**
 * GueimBoi - GameBoy emulator
 */
public class GueimBoi {
    /**
     * Emulator entry point.
     * 
     * @param args The arguments of the program.
     */
    public static void main(String[] args) { 
        MainWindow mainWindow = new MainWindow();
        mainWindow.loadROM("src/test/resources/bgbtest.gb");
        //mainWindow.loadROM("src/test/resources/blargg's_test_roms/cpu_instrs/halt_bug.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/div_write.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/rapid_toggle.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim00.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim00_div_trigger.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim01.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim01_div_trigger.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim10.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim10_div_trigger.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim11.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tim11_div_trigger.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tima_reload.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tima_write_reloading.gb");
        //mainWindow.loadROM("src/test/resources/gekkio's_test_roms/acceptance/timer/tma_write_reloading.gb");
        mainWindow.initializeGameBoy();
        mainWindow.startGameBoy();
    }

}
