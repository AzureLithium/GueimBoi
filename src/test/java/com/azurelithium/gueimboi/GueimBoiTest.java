package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple GueimBoi.
 */
public class GueimBoiTest {

    final static Logger logger = LoggerFactory.getLogger(GueimBoiTest.class);

    /**
     * Rigorous Test.
     */
    @Test
    public void testGueimBoi() {
        logger.info("Starting GueimBoi.");
        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
        cpu.tick();
    }
}
