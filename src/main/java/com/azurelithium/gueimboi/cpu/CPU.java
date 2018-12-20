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
        HALT,
        FETCH_OPCODE, 
        FETCH_OPCODE_SUFFIX, 
        RUN_INSTRUCTION        
    }

    private CPUState CPU_STATE = CPUState.FETCH_OPCODE;

    private Registers registers;
    private InterruptManager interruptManager;
    private ALU ALU;
    private MMU MMU;
    private ISA ISA;
    private ExecutionContext executionContext;

    private ListIterator<InstructionStep> instructionStepIterator;
    private boolean mustEndTick;
    private boolean halt_bug;

    public CPU(MMU _MMU) {
        registers = new Registers();
        ALU = new ALU();
        MMU = _MMU;
        ISA = new ISA();
        executionContext = new ExecutionContext();
        interruptManager = new InterruptManager(executionContext, MMU);
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
        while (!mustEndTick) {
            switch (CPU_STATE) {
                case HALT:
                    if (interruptManager.isAnyInterruptRequestedAndEnabled()) {
                        CPU_STATE = CPUState.FETCH_OPCODE;
                    }
                    return;
                case FETCH_OPCODE:
                    if (executionContext.HALT) {
                        executionContext.HALT = false;
                        if (!interruptManager.isAnyInterruptRequestedAndEnabled()) {
                            CPU_STATE = CPUState.HALT;
                            return;
                        }
                        if (!executionContext.getIME() && interruptManager.isAnyInterruptRequestedAndEnabled()) {
                            halt_bug = true;
                        }
                    }
                    ListIterator<InstructionStep> interruptVector = interruptManager.checkInterrupts();
                    if (interruptVector != null) {
                        instructionStepIterator = interruptVector;
                        CPU_STATE = CPUState.RUN_INSTRUCTION;
                        break;
                    }
                    interruptManager.checkScheduleIME();
                    fetchOpcode();
                    break;
                case FETCH_OPCODE_SUFFIX:
                    fetchOpcodeSuffix();
                    break;
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
        mustEndTick = true;
        if (!halt_bug) {
            ALU.incrementPC(executionContext, Byte.BYTES);
        } else {
            halt_bug = false;
        }
        if (opcode == 0xCB) {
            CPU_STATE = CPUState.FETCH_OPCODE_SUFFIX;
            return;
        }
        retrieveInstruction(opcode, instructionAddress);
        executeInstruction();
    }

    private void fetchOpcodeSuffix() {
        int instructionAddress = registers.getPC();
        int opcode = 0xCB00 + MMU.readByte(registers.getPC());
        mustEndTick = true;
        ALU.incrementPC(executionContext, Byte.BYTES);
        retrieveInstruction(opcode, instructionAddress);
        executeInstruction();
    }

    private void retrieveInstruction(int opcode, int instructionAddress) {
        Instruction instruction = ISA.getInstruction(opcode);
        if (instruction == null) {
            logger.error("Operation {} not recognized at address {}, aborting GueimBoi...",
                    StringUtils.toHex(opcode), StringUtils.toHex(instructionAddress));
            System.exit(1);
        }
        logger.trace("Fetched instruction {} at address {}.", instruction,
                StringUtils.toHex(instructionAddress));

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
