package com.azurelithium.gueimboi.cpu;

abstract class InstructionStep {

    abstract void execute(ExecutionContext executionContext);

}


class Load extends InstructionStep {

    private Operand operand;

    Load(Operand _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        operand.read(executionContext);
    }

}


class Store extends InstructionStep {

    private Operand operand;

    Store(Operand _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        operand.write(executionContext);
    }

}


class XOR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.XOR();
    }

}
