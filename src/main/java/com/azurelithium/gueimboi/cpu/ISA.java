package com.azurelithium.gueimboi.cpu;

import java.util.TreeMap;
import java.util.Map.Entry;

import com.azurelithium.gueimboi.utils.StringUtils;

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
        private final PC PC = new PC();
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
                addSwapInstructions();
                addBitInstructions();
        }

        private void addControlInstructions() {
                addInstruction(0x00, instructionBuilder.instruction(0x00, "NOP")); //does nothing
                addInstruction(0x76, instructionBuilder.instruction(0x76, "HALT")); //does nothing at the moment
                addInstruction(0xF3, instructionBuilder.instruction(0xF3, "DI")); //does nothing at the moment
                addInstruction(0xFB, instructionBuilder.instruction(0xFB, "EI")); //does nothing at the moment
        }

        private void addJumpInstructions() {
                addInstruction(0x18, instructionBuilder.instruction(0x18, "JR r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .jumpRelative()
                        .internalDelay());

                addInstruction(0x20, instructionBuilder.instruction(0x20, "JR NZ, r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .ifNZ()
                        .jumpRelative()
                        .internalDelay());

                addInstruction(0x28, instructionBuilder.instruction(0x28, "JR Z, r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .ifZ()
                        .jumpRelative()
                        .internalDelay());

                addInstruction(0x30, instructionBuilder.instruction(0x20, "JR NC, r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .ifNC()
                        .jumpRelative()
                        .internalDelay());

                addInstruction(0x38, instructionBuilder.instruction(0x38, "JR C, r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .ifC()
                        .jumpRelative()
                        .internalDelay());

                addInstruction(0xC3, instructionBuilder.instruction(0xC3, "JP a16")
                        .decode(WORD_ADDRESS)
                        .jump()
                        .internalDelay());

                addInstruction(0xC2, instructionBuilder.instruction(0xC2, "JP NZ, a16")
                        .decode(WORD_ADDRESS)
                        .ifNZ()
                        .jump()
                        .internalDelay());

                addInstruction(0xCA, instructionBuilder.instruction(0xCA, "JP Z, a16")
                        .decode(WORD_ADDRESS)
                        .ifZ()
                        .jump()
                        .internalDelay());

                addInstruction(0xD2, instructionBuilder.instruction(0xD2, "JP NC, a16")
                        .decode(WORD_ADDRESS)
                        .ifNC()
                        .jump()
                        .internalDelay());

                addInstruction(0xDA, instructionBuilder.instruction(0xDA, "JP C, a16")
                        .decode(WORD_ADDRESS)
                        .ifC()
                        .jump()
                        .internalDelay());

                addInstruction(0xE9, instructionBuilder.instruction(0xE9, "JP (HL)")
                        .load(HL)
                        .useDataAsAddress()
                        .jump());
        }

        private void addCallInstructions() {
                addInstruction(0xC4, instructionBuilder.instruction(0xC4, "CALL NZ, a16")
                        .decode(WORD_ADDRESS)
                        .ifNZ()
                        .internalDelay()
                        .push(PC)
                        .jump());

                addInstruction(0xCC, instructionBuilder.instruction(0xCC, "CALL Z, a16")
                        .decode(WORD_ADDRESS)
                        .ifZ()
                        .internalDelay()
                        .push(PC)
                        .jump());

                addInstruction(0xD4, instructionBuilder.instruction(0xD4, "CALL NC, a16")
                        .decode(WORD_ADDRESS)
                        .ifNC()
                        .internalDelay()
                        .push(PC)
                        .jump());

                addInstruction(0xDC, instructionBuilder.instruction(0xDC, "CALL C, a16")
                        .decode(WORD_ADDRESS)
                        .ifC()
                        .internalDelay()
                        .push(PC)
                        .jump());

                addInstruction(0xC9, instructionBuilder.instruction(0xC9, "RET")
                        .pop(PC)
                        .internalDelay());

                addInstruction(0xD9, instructionBuilder.instruction(0xD9, "RETI") //does not enable interrupts yet, they are still not supported
                        .pop(PC)
                        .internalDelay());

                addInstruction(0xCD, instructionBuilder.instruction(0xCD, "CALL a16")
                        .decode(WORD_ADDRESS)
                        .internalDelay()
                        .push(PC)
                        .jump());

                addInstruction(0xC0, instructionBuilder.instruction(0xC0, "RET NZ")
                        .internalDelay()
                        .ifNZ()
                        .pop(PC)
                        .internalDelay());

                addInstruction(0xC8, instructionBuilder.instruction(0xC8, "RET Z")
                        .internalDelay()
                        .ifZ()
                        .pop(PC)
                        .internalDelay());
                
                addInstruction(0xD0, instructionBuilder.instruction(0xD0, "RET NC")
                        .internalDelay()
                        .ifNC()
                        .pop(PC)
                        .internalDelay());

                addInstruction(0xD8, instructionBuilder.instruction(0xD8, "RET C")
                        .internalDelay()
                        .ifC()
                        .pop(PC)
                        .internalDelay());

                for (int opcode = 0xC7; opcode <= 0xFF; opcode += 0x8) {
                        addInstruction(opcode, instructionBuilder.instruction(opcode, "RST " + StringUtils.toHex(opcode - 0xC7))
                                .internalDelay()
                                .push(PC)
                                .rst(opcode - 0xC7));
                }
                
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
                                .decode(BYTE_IMMEDIATE)
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
                        .decode(BYTE_ADDRESS)
                        .load(A)
                        .store(BYTE_ADDRESS));

                addInstruction(0xF0, instructionBuilder.instruction(0xF0, "LDH A, (a8)")
                        .decode(BYTE_ADDRESS)
                        .load(BYTE_ADDRESS)
                        .store(A));

                addInstruction(0xE2, instructionBuilder.instruction(0xE2, "LD (C), A")
                        .load(A)
                        .store(C_ADDRESS));

                addInstruction(0xF2, instructionBuilder.instruction(0xF2, "LD A, (C)")
                        .load(C_ADDRESS)
                        .store(A));

                addInstruction(0xEA, instructionBuilder.instruction(0xEA, "LD (a16), A")
                        .decode(WORD_ADDRESS)
                        .load(A)
                        .store(WORD_ADDRESS));

                addInstruction(0xFA, instructionBuilder.instruction(0xFA, "LD A, (a16)")
                        .decode(WORD_ADDRESS)
                        .load(WORD_ADDRESS)
                        .store(A));    
        }

        private void addWordLoadStoreInstructions() {
                int opcode = 0x01;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "LD " + ((Operand)operand).getName() + ", d16")
                                .decode(WORD_IMMEDIATE)
                                .store(operand));
                        opcode += 0x10;
                }

                addInstruction(0x08, instructionBuilder.instruction(0x08, "LD (a16), SP")
                        .decode(WORD_ADDRESS)
                        .load(SP)
                        .storeWordInAddress(WORD_ADDRESS));

                opcode = 0xC5;
                for (PushablePopable operand : new PushablePopable[]{BC, DE, HL, AF}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "PUSH " + ((Operand)operand).getName())
                                .internalDelay()
                                .push(operand));
                        opcode += 0x10;
                }

                opcode = 0xC1;
                for (PushablePopable operand : new PushablePopable[]{BC, DE, HL, AF}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "POP " + ((Operand)operand).getName())
                                .pop(operand));
                        opcode += 0x10;
                }

                addInstruction(0xF8, instructionBuilder.instruction(0xF8, "LD HL, SP + r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .internalDelay()
                        .addToSP()
                        .store(HL));

                addInstruction(0xF9, instructionBuilder.instruction(0xF9, "LD SP, HL")
                        .load(HL)
                        .store(SP)
                        .internalDelay());
        }

        private void addByteALUInstructions() {
                addInstruction(0x27, instructionBuilder.instruction(0x27, "DAA")
                        .DAA()
                        .store(A));

                addInstruction(0x2F, instructionBuilder.instruction(0x2F, "CPL")
                        .CPL()
                        .store(A));

                addInstruction(0x37, instructionBuilder.instruction(0x37, "SCF")
                        .SCF());

                addInstruction(0x3F, instructionBuilder.instruction(0x3F, "CCF")
                        .CCF());

                int opcode = 0x04;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "INC " + ((Operand)operand).getName())
                                .load(operand)
                                .INC()
                                .store(operand));
                        opcode += 0x8;
                }

                opcode = 0x05;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "DEC " + ((Operand)operand).getName())
                                .load(operand)
                                .DEC()
                                .store(operand));
                        opcode += 0x8;
                }

                opcode = 0x80;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "ADD A, " + ((Operand)operand).getName())
                                .load(operand)
                                .ADD()
                                .store(A));
                        opcode++;
                }

                opcode = 0x88;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "ADC A, " + ((Operand)operand).getName())
                                .load(operand)
                                .ADC()
                                .store(A));
                        opcode++;
                }

                opcode = 0x90;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SUB " + ((Operand)operand).getName())
                                .load(operand)
                                .SUB()
                                .store(A));
                        opcode++;
                }

                opcode = 0x98;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SBC " + ((Operand)operand).getName())
                                .load(operand)
                                .SBC()
                                .store(A));
                        opcode++;
                }

                opcode = 0xA0;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "AND " + ((Operand)operand).getName())
                                .load(operand)
                                .AND()
                                .store(A));
                        opcode++;
                }

                opcode = 0xA8;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "XOR " + ((Operand)operand).getName())
                                .load(operand)
                                .XOR()
                                .store(A));
                        opcode++;
                }

                opcode = 0xB0;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "OR " + ((Operand)operand).getName())
                                .load(operand)
                                .OR()
                                .store(A));
                        opcode++;
                }

                opcode = 0xB8;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "CP " + ((Operand)operand).getName())
                                .load(operand)
                                .CP());
                        opcode++;
                }

                addInstruction(0xC6, instructionBuilder.instruction(0xC6, "ADD A, d8")
                        .decode(BYTE_IMMEDIATE)
                        .ADD()
                        .store(A));

                addInstruction(0xCE, instructionBuilder.instruction(0xCE, "ADC A, d8")
                        .decode(BYTE_IMMEDIATE)
                        .ADC()
                        .store(A));

                addInstruction(0xD6, instructionBuilder.instruction(0xD6, "SUB d8")
                        .decode(BYTE_IMMEDIATE)
                        .SUB()
                        .store(A));

                addInstruction(0xDE, instructionBuilder.instruction(0xDE, "SBC A, d8")
                        .decode(BYTE_IMMEDIATE)
                        .SBC()
                        .store(A));

                addInstruction(0xE6, instructionBuilder.instruction(0xE6, "AND d8")
                        .decode(BYTE_IMMEDIATE)
                        .AND()
                        .store(A));

                addInstruction(0xEE, instructionBuilder.instruction(0xEE, "XOR d8")
                        .decode(BYTE_IMMEDIATE)
                        .XOR()
                        .store(A));

                addInstruction(0xF6, instructionBuilder.instruction(0xF6, "OR d8")
                        .decode(BYTE_IMMEDIATE)
                        .OR()
                        .store(A));

                addInstruction(0xFE, instructionBuilder.instruction(0xFE, "CP d8")
                        .decode(BYTE_IMMEDIATE)
                        .CP());
        }

        private void addWordALUInstructions() {
                int opcode = 0x03;
                for (RegisterOperand operand : new RegisterOperand[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "INC " + ((Operand)operand).getName())
                                .load(operand)
                                .wordINC()
                                .store(operand)
                                .internalDelay());
                        opcode += 0x10;
                }

                opcode = 0x09;
                for (ReadableWritable operand : new ReadableWritable[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "ADD HL, " + ((Operand)operand).getName())
                                .load(operand)
                                .wordADD()
                                .store(HL)
                                .internalDelay());
                        opcode += 0x10;
                }

                opcode = 0x0B;
                for (RegisterOperand operand : new RegisterOperand[]{BC, DE, HL, SP}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "DEC " + ((Operand)operand).getName())
                                .load(operand)
                                .wordDEC()
                                .store(operand)
                                .internalDelay());
                        opcode += 0x10;
                }

                addInstruction(0xE8, instructionBuilder.instruction(0xE8, "ADD SP, r8")
                        .decode(SIGNEDBYTE_IMMEDIATE)
                        .internalDelay()
                        .internalDelay()
                        .addToSP()
                        .store(SP));

        }

        private void addRotationInstructions() {
                addInstruction(0x07, instructionBuilder.instruction(0x07, "RLCA")
                        .RLCA()
                        .store(A));

                addInstruction(0x0F, instructionBuilder.instruction(0x0F, "RRCA")
                        .RRCA()
                        .store(A));

                addInstruction(0x17, instructionBuilder.instruction(0x17, "RLA")
                        .RLA()
                        .store(A));                

                addInstruction(0x1F, instructionBuilder.instruction(0x1F, "RRA")
                        .RRA()
                        .store(A));

                int opcode = 0xCB00;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "RLC " + ((Operand)operand).getName())
                                .load(operand)
                                .RLC()
                                .store(operand));
                        opcode++;
                }
        
                opcode = 0xCB08;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "RRC " + ((Operand)operand).getName())
                                .load(operand)
                                .RRC()
                                .store(operand));
                        opcode++;
                }

                opcode = 0xCB10;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "RL " + ((Operand)operand).getName())
                                .load(operand)
                                .RL()
                                .store(operand));
                        opcode++;
                }

                opcode = 0xCB18;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "RR " + ((Operand)operand).getName())
                                .load(operand)
                                .RR()
                                .store(operand));
                        opcode++;
                }
        }

        private void addShiftInstructions() {
                int opcode = 0xCB20;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SLA " + ((Operand)operand).getName())
                                .load(operand)
                                .SLA()
                                .store(operand));
                        opcode++;
                }

                opcode = 0xCB28;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SRA " + ((Operand)operand).getName())
                                .load(operand)
                                .SRA()
                                .store(operand));
                        opcode++;
                }

                opcode = 0xCB38;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SRL " + ((Operand)operand).getName())
                                .load(operand)
                                .SRL()
                                .store(operand));
                        opcode++;
                }
        }

        private void addSwapInstructions() {
                int opcode = 0xCB30;
                for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                        addInstruction(opcode, instructionBuilder.instruction(
                                opcode, "SWAP " + ((Operand)operand).getName())
                                .load(operand)
                                .SWAP()
                                .store(operand));
                        opcode++;
                }
        }

        private void addBitInstructions(){
                int opcode = 0xCB40;
                for (int i=0; i<8; i++) {
                        for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                                addInstruction(opcode, instructionBuilder.instruction(opcode, "BIT " + i + ", "+ ((Operand)operand).getName())
                                        .load(operand)
                                        .BIT(i));
                                opcode++;
                        }                        
                }

                opcode = 0xCB80;
                for (int i=0; i<8; i++) {
                        for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                                addInstruction(opcode, instructionBuilder.instruction(opcode, "RES " + i + ", "+ ((Operand)operand).getName())
                                        .load(operand)
                                        .RES(i)
                                        .store(operand));
                                opcode++;
                        }                        
                }

                opcode = 0xCBC0;
                for (int i=0; i<8; i++) {
                        for (ReadableWritable operand : new ReadableWritable[]{B, C, D, E, H, L, HL_ADDRESS, A}) {
                                addInstruction(opcode, instructionBuilder.instruction(opcode, "SET " + i + ", "+ ((Operand)operand).getName())
                                        .load(operand)
                                        .SET(i)
                                        .store(operand));
                                opcode++;
                        }                        
                }

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
