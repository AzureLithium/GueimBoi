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
        private final AF AF = new AF();
        private final BC BC = new BC();
        private final BCAddress BC_ADDRESS = new BCAddress();
        private final DE DE = new DE();
        private final DEAddress DE_ADDRESS = new DEAddress(); 
        private final HL HL = new HL();
        private final HLAddress HL_ADDRESS = new HLAddress(); 
        private final SP SP = new SP();               
        private final ByteImmediate BYTE_IMMEDIATE = new ByteImmediate();
        private final ByteAddress BYTE_ADDRESS = new ByteAddress();
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
                addByteALUInstructions();
                addWordALUInstructions();
                addRotationInstructions();
                addShiftInstructions();
                addBitInstructions();
        }

        private void addControlInstructions() {
                addInstruction(0x76, instructionBuilder.instruction(0x76, "HALT")); //does nothing at the moment
        }

        private void addJumpInstructions() {
                addInstruction(0x20, instructionBuilder.instruction(0x20, "JR NZ, r8")
                        .decodeOperand(SIGNEDBYTE_IMMEDIATE)
                        .ifNZ()
                        .jumpRelative());
        }

        private void addCallInstructions() {
                addInstruction(0xCD, instructionBuilder.instruction(0xCD, "CALL a16")
                        .decodeOperand(WORD_ADDRESS)
                        .call());

                addInstruction(0xC9, instructionBuilder.instruction(0xC9, "RET")
                        .ret());
        }

        private void addByteLoadStoreInstructions() {
                addInstruction(0x02, instructionBuilder.instruction(0x02, "LD (BC), A")
                        .load(A)
                        .store(BC_ADDRESS));

                addInstruction(0x12, instructionBuilder.instruction(0x12, "LD (DE), A")
                        .load(A)
                        .store(DE_ADDRESS));

                addInstruction(0x22, instructionBuilder.instruction(0x22, "LD (HL+), A")
                        .load(A)
                        .store(HL_ADDRESS)
                        .postIncrementHL()); //no extra cycles

                addInstruction(0x32, instructionBuilder.instruction(0x32, "LD (HL-), A")
                        .load(A)
                        .store(HL_ADDRESS)
                        .postDecrementHL()); //no extra cycles
                
                int opcode = 0x06;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "LD " + ((Operand)operand).getName() + ", d8")
                                .decodeOperand(BYTE_IMMEDIATE)
                                .store(operand));
                        opcode += 0x8;
                }
                
                addInstruction(0x0A, instructionBuilder.instruction(0x0A, "LD A, (BC)")
                        .load(BC_ADDRESS)
                        .store(A));

                addInstruction(0x1A, instructionBuilder.instruction(0x1A, "LD A, (DE)")
                        .load(DE_ADDRESS)
                        .store(A));

                addInstruction(0x2A, instructionBuilder.instruction(0x2A, "LD A, (HL+)")
                        .load(HL_ADDRESS)
                        .store(A)
                        .postIncrementHL()); //no extra cycles

                addInstruction(0x3A, instructionBuilder.instruction(0x3A, "LD A, (HL-)")
                        .load(HL_ADDRESS)
                        .store(A)
                        .postDecrementHL()); //no extra cycles

                opcode = 0x40;
                for (ReadableWritable storeOperand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        for (ReadableWritable loadOperand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                                addInstruction(opcode, instructionBuilder.instruction(
                                        opcode, "LD " + ((Operand)storeOperand).getName() + ", " + ((Operand)loadOperand).getName())
                                        .load(loadOperand)
                                        .store(storeOperand));
                                        opcode++;
                        }                        
                }

                addInstruction(0xE0, instructionBuilder.instruction(0xE0, "LDH (a8), A")
                        .decodeOperand(BYTE_ADDRESS)
                        .load(A)
                        .store(BYTE_ADDRESS));

                addInstruction(0xF0, instructionBuilder.instruction(0xF0, "LDH A, (a8)")
                        .decodeOperand(BYTE_ADDRESS)
                        .load(BYTE_ADDRESS)
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
                int opcode = 0x01;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "LD " + ((Operand)operand).getName() + ", d16")
                                .decodeOperand(WORD_IMMEDIATE)
                                .store(operand));
                        opcode += 0x10;
                }

                opcode = 0xC5;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, AF}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "PUSH " + ((Operand)operand).getName())
                                .load(operand)
                                .push());
                        opcode += 0x10;
                }

                opcode = 0xC1;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, AF}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "POP " + ((Operand)operand).getName())
                                .load(operand)
                                .pop());
                        opcode += 0x10;
                }
        }

        private void addByteALUInstructions() {
                int opcode = 0x04;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "INC " + ((Operand)operand).getName())
                                .load(operand)
                                .byteALUIncrement()
                                .store(operand));
                        opcode += 0x8;
                }

                opcode = 0x05;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "DEC " + ((Operand)operand).getName())
                                .load(operand)
                                .byteALUDecrement()
                                .store(operand));
                        opcode += 0x8;
                }

                addInstruction(0xAF, instructionBuilder.instruction(0xAF, "XOR A")
                        .load(A)
                        .XOR()
                        .store(A));

                addInstruction(0xFE, instructionBuilder.instruction(0xFE, "CP d8")
                        .decodeOperand(BYTE_IMMEDIATE)
                        .CP());
        }

        private void addWordALUInstructions() {
                int opcode = 0x03;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "INC " + ((Operand)operand).getName())
                                .load(operand)
                                .wordALUIncrement()
                                .store(operand));
                        opcode += 0x10;
                }

                opcode = 0x0B;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "DEC " + ((Operand)operand).getName())
                                .load(operand)
                                .wordALUDecrement()
                                .store(operand));
                        opcode += 0x10;
                }

        }

        private void addRotationInstructions() {
                addInstruction(0x17, instructionBuilder.instruction(0x17, "RLA")
                                .RLA());

                int opcode = 0xCB10;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "RL " + ((Operand)operand).getName())
                                .load(operand)
                                .RL()
                                .store(operand));
                        opcode++;
                }
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
                        System.out.printf("Instruction 0x%X => %s%n", entry.getKey(), entry.getValue().getMnemonic());
                }
        }
}
