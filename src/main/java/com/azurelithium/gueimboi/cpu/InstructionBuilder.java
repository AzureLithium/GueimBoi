package com.azurelithium.gueimboi.cpu;

class InstructionBuilder {

    private Instruction instruction;

    /**
     * Create instruction
     */

    InstructionBuilder instruction(int opcode, String mnemonic) {
        instruction = new Instruction(opcode, mnemonic);
        return this;
    }

    /**
     * Decode operand command
     */

    InstructionBuilder decodeOperand(Operand operand) {
        instruction.setInstructionOperand(operand);
        return this;
    }

    /**
     * Loading and Storing commands
     */

    InstructionBuilder load(Operand operand) {
        instruction.addStep(new Load(operand));
        return this;
    }

    InstructionBuilder store(Operand operand) {
        instruction.addStep(new Store(operand));
        return this;
    }

    /**
     * Arithmetic/Logical commands
     */

    InstructionBuilder XOR() {
        instruction.addStep(new XOR());
        return this;
    }

    /**
     * Build instruction
     */

    Instruction build() {
        return instruction;
    }

}
