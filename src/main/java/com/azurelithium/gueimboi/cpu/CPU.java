package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU {

    final Logger logger = LoggerFactory.getLogger(CPU.class);

    private Registers registers;
    private ISA isa;

    private MMU mmu;

    public CPU(MMU _mmu) {
        registers = new Registers();
        isa = new ISA(registers);
        mmu = _mmu;
    }

    public void runInstruction() {
        Instruction instruction = isa.getInstruction(0x7F);
        if (instruction == null) {
            logger.error("Unrecognized operation, aborting GueimBoi...");
            System.exit(1);
        }
        instruction.execute();
        logger.info("Exiting GueimBoi.");
        System.exit(0);        
    }
}