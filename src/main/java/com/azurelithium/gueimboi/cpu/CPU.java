package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU {

    final Logger logger = LoggerFactory.getLogger(CPU.class);

    private Registers registers;
    private ALU ALU;
    private MMU MMU;
    private ISA ISA;

    private static ExecutionContext executionContext;

    public CPU(MMU _MMU) {        
        registers = new Registers();
        executionContext = new ExecutionContext();
        ALU = new ALU(executionContext);
        MMU = _MMU;        
        ISA = new ISA();
        initializeExecutionContext();      
    }

    private void initializeExecutionContext() {        
        executionContext.registers = registers;
        executionContext.ALU = ALU;
        executionContext.MMU = MMU;        
    }

    public void run() {
        Instruction instruction = fetchInstruction();
        decodeInstruction(instruction);
        executeInstruction(instruction);
    }

    private Instruction fetchInstruction() {
        int instructionAddress = registers.getPC();

        int opcode = MMU.readByte(registers.getPC());
        registers.incrementPC(Byte.BYTES);

        if (opcode == 0xCB) {
            opcode = MMU.readByte(registers.getPC());
            registers.incrementPC(Byte.BYTES);
        }

        Instruction instruction = ISA.getInstruction(opcode);
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
        instruction.decode(executionContext);
    }

    private void executeInstruction(Instruction instruction) {
        instruction.execute(executionContext);
    }

}
