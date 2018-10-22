package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import com.azurelithium.gueimboi.utils.StringUtils;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction {

    final Logger logger = LoggerFactory.getLogger(Instruction.class);

    class InstructionContext {

        int[] data;
        int dataAddress;
        int cycles;
        Registers registers;
        MMU mmu;

        String printData() {
            return StringUtils.toHex(data);
        }

        String printDataAddress() {
            return StringUtils.toHex(dataAddress);
        }

    }

    private int opcode;
    private String mnemonic;
    private InstructionContext instructionContext;
    private Operand instructionOperand;
    private List<InstructionStep> instructionSteps;

    Instruction(int _opcode, String _mnemonic, Registers _registers, MMU _mmu) {
        opcode = _opcode;
        mnemonic = _mnemonic;
        instructionSteps = new LinkedList<InstructionStep>();
        instructionContext = new InstructionContext() {
            {
                registers = _registers;
                mmu = _mmu;
            }
        };
    }

    String getMnemonic() {
        return mnemonic;
    }

    void setInstructionOperand(Operand operand) {
        instructionOperand = operand;
    }

    void addStep(InstructionStep instructionStep) {
        instructionStep.setInstructionContext(instructionContext);
        instructionSteps.add(instructionStep);
    }

    void decode() {
        if (instructionOperand != null) {
            instructionOperand.decode(instructionContext);
        }
    }

    void execute() {
        logger.debug("Executing instruction 0x{} : {}", String.format("%h", opcode).toUpperCase(),
                mnemonic);
        for (InstructionStep instructionStep : instructionSteps) {
            instructionStep.execute();
        }
    }

}
