package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.memory.MMU;
import java.util.HashMap;

public class ISA {

        private HashMap<Integer, Instruction> instructions;

        public ISA(Registers _registers, MMU _mmu) {

                instructions = new HashMap<Integer, Instruction>();

                instructions.put(0x31,
                        new InstructionBuilder(0x31, "LD SP, d16", _registers, _mmu)
                                .decodeOperand(Operand.WORD)
                                .load(Operand.WORD)
                                .store(Operand.SP)
                                .build());

                instructions.put(0x7F, 
                        new InstructionBuilder(0x7F, "LD A, A", _registers, _mmu)
                                .load(Operand.A)
                                .store(Operand.A)
                                .build());

                instructions.put(0xEA,
                        new InstructionBuilder(0xEA, "LD (a16), A", _registers, _mmu)
                                .decodeOperand(Operand.WORD_ADDRESS)
                                .load(Operand.A)
                                .store(Operand.WORD_ADDRESS)
                                .build());

                instructions.put(0xFA, 
                        new InstructionBuilder(0xFA, "LD A, (a16)", _registers, _mmu)
                                .decodeOperand(Operand.WORD_ADDRESS)
                                .load(Operand.WORD_ADDRESS)
                                .store(Operand.A)
                                .build());
        }

        public Instruction getInstruction(int opcode) {
                return instructions.getOrDefault(opcode, null);
        }
}
