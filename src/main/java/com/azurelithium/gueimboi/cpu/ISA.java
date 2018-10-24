package com.azurelithium.gueimboi.cpu;

import java.util.HashMap;

class ISA {

        private final Operand A = Operand.A;
        private final Operand SP = Operand.SP;
        private final Operand WORD = Operand.WORD;
        private final Operand WORD_ADDRESS = Operand.WORD_ADDRESS;

        private final InstructionBuilder instructionBuilder = new InstructionBuilder();
        private HashMap<Integer, Instruction> instructions;


        ISA() {

                instructions = new HashMap<Integer, Instruction>();

                instructions.put(0x31, instructionBuilder.instruction(0x31, "LD SP, d16")
                        .decodeOperand(WORD)
                        .load(WORD)
                        .store(SP)
                        .build());

                instructions.put(0x7F, instructionBuilder.instruction(0x7F, "LD A, A")
                        .load(A)
                        .store(A)
                        .build());

                instructions.put(0xAF, instructionBuilder.instruction(0xAF, "XOR A")
                        .load(A)
                        .XOR()
                        .store(A)
                        .build());

                instructions.put(0xEA, instructionBuilder.instruction(0xEA, "LD (a16), A")
                        .decodeOperand(WORD_ADDRESS)
                        .load(A)
                        .store(WORD_ADDRESS)
                        .build());

                instructions.put(0xFA, instructionBuilder.instruction(0xFA, "LD A, (a16)")
                        .decodeOperand(WORD_ADDRESS)
                        .load(WORD_ADDRESS)
                        .store(A)
                        .build());
        }

        Instruction getInstruction(int opcode) {
                return instructions.getOrDefault(opcode, null);
        }
}
