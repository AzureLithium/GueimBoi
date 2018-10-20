package com.azurelithium.gueimboi.cpu;

public class InstructionBuilder {

    private Instruction instruction;
    
    public InstructionBuilder(int opcode, String mnemonic, Registers registers) {
        instruction = new Instruction(opcode, mnemonic, registers);
    }

    public InstructionBuilder load(Operand operand) {
        instruction.addStep(new Load(operand));
        return this;
    }

    public InstructionBuilder store(Operand operand) {
        instruction.addStep(new Store(operand));
        return this;
    }

    public Instruction build() {
        return instruction;
    }
    
}