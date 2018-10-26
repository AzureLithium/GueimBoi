package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private Registers registers;
    private ALU ALU;
    private MMU MMU;
    private ISA ISA;

    private static ExecutionContext executionContext;

    public CPU(MMU _MMU) {        
        registers = new Registers();    
        ALU = new ALU();
        MMU = _MMU;        
        ISA = new ISA();
        executionContext = new ExecutionContext();
        initializeExecutionContext();      
    }

    private void initializeExecutionContext() {
        executionContext.executeNextStep = true;      
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
            opcode = ByteUtils.toWord(opcode, MMU.readByte(registers.getPC()));
            registers.incrementPC(Byte.BYTES);
        }

        Instruction instruction = ISA.getInstruction(opcode);
        if (instruction == null) {
            logger.error("Operation {} not recognized at address {}, aborting GueimBoi...",
                    StringUtils.toHex(opcode), StringUtils.toHex(instructionAddress));
            System.exit(1);
        }

        logger.trace("Fetched instruction {} : {} at address {}.", StringUtils.toHex(opcode),
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
