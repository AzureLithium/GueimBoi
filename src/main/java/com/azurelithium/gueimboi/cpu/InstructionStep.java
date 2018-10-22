package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.cpu.Instruction.InstructionContext;

public abstract class InstructionStep {

    protected InstructionContext instructionContext;

    public void setInstructionContext(InstructionContext _instructionContext) {
        instructionContext = _instructionContext;
    }

    public abstract void execute();

}


class Load extends InstructionStep {

    private Operand operand;

    public Load(Operand _operand) {
        operand = _operand;
    }

    public void execute() {
        operand.read(instructionContext);
    }

}


class Store extends InstructionStep {

    private Operand operand;

    public Store(Operand _operand) {
        operand = _operand;
    }

    public void execute() {
        operand.write(instructionContext);
    }

}
