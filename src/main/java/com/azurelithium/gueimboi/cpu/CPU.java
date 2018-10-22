package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU {

    final Logger logger = LoggerFactory.getLogger(CPU.class);

    private Registers registers;
    private ISA isa;

    private MMU mmu;

    public CPU(MMU _mmu) {
        mmu = _mmu;
        registers = new Registers();
        isa = new ISA(registers, mmu);
    }

    public void run() {
        Instruction instruction = fetchInstruction();
        decodeInstruction(instruction);
        executeInstruction(instruction);
    }

    private Instruction fetchInstruction() {
        int instructionAddress = registers.getPC();

        int opcode = mmu.readByte(registers.getPC());
        registers.incrementPC(Byte.BYTES);

        if (opcode == 0xCB) {
            opcode = mmu.readByte(registers.getPC());
            registers.incrementPC(Byte.BYTES);
        }

        Instruction instruction = isa.getInstruction(opcode);
        if (instruction == null) {
            logger.error("Operation {} not recognized, aborting GueimBoi...",
                    StringUtils.toHex(opcode));
            System.exit(1);
        }

        logger.debug("Fetched instruction {} : {} at address {}.", StringUtils.toHex(opcode),
                instruction.getMnemonic(), StringUtils.toHex(instructionAddress));
        return instruction;
    }

    private void decodeInstruction(Instruction instruction) {
        instruction.decode();
    }

    private void executeInstruction(Instruction instruction) {
        instruction.execute();
    }

}
