package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Instruction {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private int opcode;
    private String mnemonic;
    private List<InstructionStep> instructionSteps;

    Instruction(int _opcode, String _mnemonic) {
        opcode = _opcode;
        mnemonic = _mnemonic;
        instructionSteps = new LinkedList<InstructionStep>();
    }

    String getMnemonic() {
        return mnemonic;
    }

    ListIterator<InstructionStep> getInstructionStepIterator() {
        return instructionSteps.listIterator();
    }

    void addStep(InstructionStep instructionStep) {
        instructionSteps.add(instructionStep);
    }

    public String toString() {
        return StringUtils.toHex(opcode) + " : " + mnemonic;
    }

}
