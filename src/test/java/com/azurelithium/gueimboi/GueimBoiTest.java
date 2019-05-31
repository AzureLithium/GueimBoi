package com.azurelithium.gueimboi;

import static org.junit.Assert.fail;
import com.azurelithium.gueimboi.gui.MainWindow;
import com.azurelithium.gueimboi.utils.TestResultChecker;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for simple GueimBoi.
 */
public class GueimBoiTest {

    final static Logger logger = LoggerFactory.getLogger(GueimBoiTest.class);
    final static String testResourcesPath = "D:/gueimboi/src/test/resources/";
    // final static String testResourcesPath = "/media/jlario/JAVI/gueimboi/src/test/resources/";

    @Test
    public void testBlarggCPUInstrs_01_SPECIAL() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/01-special.gb");
    }

    @Test
    public void testBlarggCPUInstrs_02_INTERRUPTS() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/02-interrupts.gb");
    }

    @Test
    public void testBlarggCPUInstrs_03_OP_SP_HL() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/03-op sp,hl.gb");
    }

    @Test
    public void testBlarggCPUInstrs_04_OP_R_IMM() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/04-op r,imm.gb");
    }

    @Test
    public void testBlarggCPUInstrs_05_OP_RP() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/05-op rp.gb");
    }

    @Test
    public void testBlarggCPUInstrs_06_LD_R_R() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/06-ld r,r.gb");
    }

    @Test
    public void testBlarggCPUInstrs_07_JR_JP_CALL_RET_RST() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/07-jr,jp,call,ret,rst.gb");
    }

    @Test
    public void testBlarggCPUInstrs_08_MISC_INSTRS() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/08-misc instrs.gb");
    }

    @Test
    public void testBlarggCPUInstrs_09_OP_R_R() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/09-op r,r.gb");
    }

    @Test
    public void testBlarggCPUInstrs_10_BIT_OPS() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/10-bit ops.gb");
    }

    @Test
    public void testBlarggCPUInstrs_11_OP_A_HLADDRESS() {
        testROM(testResourcesPath + "blargg's_test_roms/cpu_instrs/11-op a,(hl).gb");
    }

    @Test
    public void testBlarggInstrTiming() {
        testROM(testResourcesPath + "blargg's_test_roms/instr_timing/instr_timing.gb");
    }

    @Test
    public void testBlarggMemTiming_01_READ_TIMING() {
        testROM(testResourcesPath + "blargg's_test_roms/mem_timing/01-read_timing.gb");
    }

    @Test
    public void testBlarggMemTiming_02_WRITE_TIMING() {
        testROM(testResourcesPath + "blargg's_test_roms/mem_timing/02-write_timing.gb");
    }

    @Test
    public void testBlarggMemTiming_03_MODIFY_TIMING() {
        testROM(testResourcesPath + "blargg's_test_roms/mem_timing/03-modify_timing.gb");
    }    

    private void testROM(String ROMPath) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.loadROM(ROMPath);  
        mainWindow.initializeGameBoy();
        mainWindow.startGameBoy();
        TestResultChecker trc = new TestResultChecker(mainWindow.getSerialContent());
        boolean result = trc.call();
        mainWindow.stopGameBoy();
        mainWindow.close();
        if (!result) {
            fail();
        }
    }
}
