package com.azurelithium.gueimboi.cpu;

import java.util.ListIterator;
import com.azurelithium.gueimboi.common.Component;
import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CPU extends Component {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private enum CPUState {
        FETCH_OPCODE,
        FETCH_OPCODE_SUFFIX,
        RUN_INSTRUCTION
    }
    private CPUState CPU_STATE = CPUState.FETCH_OPCODE;

    private Registers registers;
    private ALU ALU;
    private MMU MMU;
    private ISA ISA;
    private ExecutionContext executionContext;

    private ListIterator<InstructionStep> instructionStepIterator;
    private boolean mustEndTick;

    public CPU(MMU _MMU) {        
        registers = new Registers();    
        ALU = new ALU();
        MMU = _MMU;        
        ISA = new ISA();
        executionContext = new ExecutionContext();
        initializeExecutionContext();      
    }

    public ExecutionContext getExecutionContext() {
        return executionContext;
    }

    private void initializeExecutionContext() {
        executionContext.executeNextStep = true;      
        executionContext.registers = registers;
        executionContext.ALU = ALU;
        executionContext.MMU = MMU;        
    }

    public int getPC() {
        return executionContext.registers.getPC();
    }

    public void tick() {
        while(!mustEndTick) {
            switch (CPU_STATE) {
                case FETCH_OPCODE:
                    fetchOpcode();
                    return;
                case FETCH_OPCODE_SUFFIX:
                    fetchOpcodeSuffix();
                    return;
                case RUN_INSTRUCTION:
                    executeInstruction();
                    break;
            }
        }
        mustEndTick = false;     
    }

    private void fetchOpcode() {
        int instructionAddress = registers.getPC();
        int opcode = MMU.readByte(registers.getPC());
        ALU.incrementPC(executionContext, Byte.BYTES);
        executionContext.addCycles();
        if (opcode == 0xCB) {
            CPU_STATE = CPUState.FETCH_OPCODE_SUFFIX;
            return;
        }
        retrieveInstruction(opcode, instructionAddress);
   }

    private void fetchOpcodeSuffix() {
        int instructionAddress = registers.getPC();
        int opcode = 0xCB00 + MMU.readByte(registers.getPC());
        ALU.incrementPC(executionContext, Byte.BYTES);
        executionContext.addCycles();
        retrieveInstruction(opcode, instructionAddress);
    }

    private void retrieveInstruction(int opcode, int instructionAddress) {
        Instruction instruction = ISA.getInstruction(opcode);
        if (instruction == null) {
            logger.error("Operation {} not recognized at address {}, aborting GueimBoi...",
                    StringUtils.toHex(opcode), StringUtils.toHex(instructionAddress));
            logger.info("Cycles: {}", executionContext.getCycles());
            System.exit(1);
        }

        logger.trace("Fetched instruction {} at address {}.", instruction, StringUtils.toHex(instructionAddress));

        instructionStepIterator = instruction.getInstructionStepIterator();
        CPU_STATE = CPUState.RUN_INSTRUCTION;
    }

    private void executeInstruction() {  
        while (instructionStepIterator.hasNext() && executionContext.executeNextStep) {
            InstructionStep instructionStep = instructionStepIterator.next();  
            if (!instructionStep.doesConsumeCycles()) {
                instructionStep.execute(executionContext);
            } else if (!mustEndTick) {
                instructionStep.execute(executionContext);
                executionContext.addCycles();
                mustEndTick = true;
            } else {
                instructionStepIterator.previous();
                return;
            }    
        }
        executionContext.executeNextStep = true; 
        CPU_STATE = CPUState.FETCH_OPCODE;
    }

}
