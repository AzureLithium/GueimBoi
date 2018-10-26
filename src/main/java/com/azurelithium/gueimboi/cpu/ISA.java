package com.azurelithium.gueimboi.cpu;

import java.util.TreeMap;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ISA {

        final Logger logger = LoggerFactory.getLogger(getClass());

        private final InstructionBuilder instructionBuilder = new InstructionBuilder();
        private TreeMap<Integer, Instruction> instructions;

        private final A A = new A();
        private final B B = new B();
        private final C C = new C();
        private final CAddress C_ADDRESS = new CAddress();
        private final D D = new D();
        private final E E = new E();   
        private final H H = new H();
        private final L L = new L();
        private final HL HL = new HL();
        private final HLAddress HL_ADDRESS = new HLAddress(); 
        private final SP SP = new SP();               
        private final ByteImmediate BYTE_IMMEDIATE = new ByteImmediate();
        private final SignedByteImmediate SIGNEDBYTE_IMMEDIATE = new SignedByteImmediate();
        private final WordImmediate WORD_IMMEDIATE = new WordImmediate();
        private final WordAddress WORD_ADDRESS = new WordAddress();      

        ISA() {
                instructions = new TreeMap<Integer, Instruction>();

                addControlInstructions();
                addJumpInstructions();
                addCallInstructions();
                addByteLoadStoreInstructions();
                addWordLoadStoreInstructions();
                addByteArithmeticalLogicalInstructions();
                addWordArithmeticalLogicalInstructions();
                addRotationInstructions();
                addShiftInstructions();
                addBitInstructions();         
        }

        private void addControlInstructions() {

        }

        private void addJumpInstructions() {
                addInstruction(0x20, instructionBuilder.instruction(0x20, "JR NZ, r8")
                        .decodeOperand(SIGNEDBYTE_IMMEDIATE)
                        .ifNZ()
                        .jumpRelative());
        }

        private void addCallInstructions() {

        }

        private void addByteLoadStoreInstructions() {
                int opcode = 0x06;
                for (Writable operand : new Writable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "LD " + ((Operand)operand).getName() + ", d8")
                                .decodeOperand(BYTE_IMMEDIATE)
                                .store(operand));
                        opcode += 0x8;
                }

                addInstruction(0x32, instructionBuilder.instruction(0x32, "LD (HL-), A")
                        .load(A)
                        .store(HL_ADDRESS)
                        .load(HL)
                        .decrement()
                        .store(HL));

                addInstruction(0x7F, instructionBuilder.instruction(0x7F, "LD A, A")
                        .load(A)
                        .store(A));

                addInstruction(0xE2, instructionBuilder.instruction(0xE2, "LD (C), A")
                        .load(A)
                        .store(C_ADDRESS));

                addInstruction(0xF2, instructionBuilder.instruction(0xF2, "LD A, (C)")
                        .load(C_ADDRESS)
                        .store(A));

                addInstruction(0xEA, instructionBuilder.instruction(0xEA, "LD (a16), A")
                        .decodeOperand(WORD_ADDRESS)
                        .load(A)
                        .store(WORD_ADDRESS));

                addInstruction(0xFA, instructionBuilder.instruction(0xFA, "LD A, (a16)")
                        .decodeOperand(WORD_ADDRESS)
                        .load(WORD_ADDRESS)
                        .store(A));    
        }

        private void addWordLoadStoreInstructions() {
                addInstruction(0x21, instructionBuilder.instruction(0x21, "LD HL, d16")
                        .decodeOperand(WORD_IMMEDIATE)
                        .store(HL));

                addInstruction(0x31, instructionBuilder.instruction(0x31, "LD SP, d16")
                        .decodeOperand(WORD_IMMEDIATE)
                        .store(SP));
        }

        private void addByteArithmeticalLogicalInstructions() {
                addInstruction(0xAF, instructionBuilder.instruction(0xAF, "XOR A")
                        .load(A)
                        .XOR()
                        .store(A));
        }

        private void addWordArithmeticalLogicalInstructions() {

        }

        private void addRotationInstructions() {

        }

        private void addShiftInstructions() {

        }

        private void addBitInstructions(){
                addInstruction(0xCB7C, instructionBuilder.instruction(0xCB7C, "BIT 7, H")
                        .load(H)
                        .testBit(7));
        }

        private void addInstruction(int opcode, InstructionBuilder instructionBuilder) {
                instructions.putIfAbsent(opcode, instructionBuilder.build());
        }

        Instruction getInstruction(int opcode) {
                return instructions.getOrDefault(opcode, null);
        }

        private void printISA() {
                for (Entry<Integer, Instruction> entry : instructions.entrySet()) {
                        logger.trace("Instruction %s => %s%n", entry.getKey(), entry.getValue().getMnemonic());
                }
        }
}
