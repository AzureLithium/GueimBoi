package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;

public class CPU {

    private Registers registers;
    

    private MMU mmu;

    public CPU(MMU _mmu) {
        registers = new Registers();
        mmu = _mmu;
    }

    public void runInstruction() {
        
    }
}