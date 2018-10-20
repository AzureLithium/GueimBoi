package com.azurelithium.gueimboi.cpu;

import java.util.HashMap;

public class ISA {

    private HashMap<Integer, Instruction> instructions;
    
    public ISA(Registers _registers) {
        instructions = new HashMap<Integer, Instruction>();
        instructions.put(0x7F, new InstructionBuilder(0x7F, "LD A, A", _registers).load(Operand.A).store(Operand.A).build());
    }

    public Instruction getInstruction(int opcode) {
        return instructions.getOrDefault(opcode, null);
    }
}