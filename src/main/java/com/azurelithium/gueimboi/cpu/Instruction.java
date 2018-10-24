package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction {

    final Logger logger = LoggerFactory.getLogger(Instruction.class);

    private int opcode;
    private String mnemonic;
    private Operand instructionOperand;
    private List<InstructionStep> instructionSteps;

    Instruction(int _opcode, String _mnemonic) {
        opcode = _opcode;
        mnemonic = _mnemonic;
        instructionSteps = new LinkedList<InstructionStep>();
    }

    String getMnemonic() {
        return mnemonic;
    }

    void setInstructionOperand(Operand operand) {
        instructionOperand = operand;
    }

    void addStep(InstructionStep instructionStep) {
        instructionSteps.add(instructionStep);
    }

    void decode(ExecutionContext executionContext) {
        if (instructionOperand != null) {
            instructionOperand.decode(executionContext);
        }
    }

    void execute(ExecutionContext executionContext) {
        logger.debug("Executing instruction {} : {}", StringUtils.toHex(opcode),
                mnemonic);
        for (InstructionStep instructionStep : instructionSteps) {
            instructionStep.execute(executionContext);
        }
    }

}
