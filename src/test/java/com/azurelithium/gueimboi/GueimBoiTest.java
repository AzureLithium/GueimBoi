package com.azurelithium.gueimboi;

import com.azurelithium.gueimboi.cpu.CPU;
import com.azurelithium.gueimboi.memory.MMU;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple GueimBoi.
 */
public class GueimBoiTest {
    /**
     * Rigorous Test.
     */
    @Test
    public void testGueimBoi() {
        MMU mmu = new MMU();
        CPU cpu = new CPU(mmu);
    }
}
