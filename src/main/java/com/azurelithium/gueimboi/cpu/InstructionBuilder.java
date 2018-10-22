package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;

public class InstructionBuilder {

    private Instruction instruction;

    public InstructionBuilder(int opcode, String mnemonic, Registers registers, MMU mmu) {
        instruction = new Instruction(opcode, mnemonic, registers, mmu);
    }

    public InstructionBuilder decodeOperand(Operand operand) {
        instruction.setInstructionOperand(operand);
        return this;
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
