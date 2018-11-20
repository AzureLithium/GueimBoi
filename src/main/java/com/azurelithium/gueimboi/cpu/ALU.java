package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ALU {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private final int BYTE_MASK = 0xFF;
    private final int WORD_MASK = 0xFFFF;
    private final int BYTE_HALFCARRY_POSITION = Byte.SIZE / 2 - 1;
    private final int BYTE_CARRY_POSITION = Byte.SIZE - 1;
    private final int WORD_HALFCARRY_POSITION = Short.SIZE / 2 + BYTE_HALFCARRY_POSITION;
    private final int WORD_CARRY_POSITION = Short.SIZE - 1;

    void INC(ExecutionContext executionContext) {
        int byteData = executionContext.getData();
        int incrementResult = (byteData + 1) & BYTE_MASK;
        executionContext.setData(incrementResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(incrementResult == 0);
        flags.resetN();
        flags.setH(carry(byteData, 1, BYTE_HALFCARRY_POSITION));
        logALUOperation("Byte Increment", StringUtils.toHex(incrementResult), flags);
    }

    void DEC(ExecutionContext executionContext) {
        int byteData = executionContext.getData();
        int decrementResult = (byteData - 1) & BYTE_MASK;
        executionContext.setData(decrementResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(decrementResult == 0);
        flags.setN();
        flags.setH(borrow(byteData, 1, BYTE_HALFCARRY_POSITION));
        logALUOperation("Byte Decrement", StringUtils.toHex(decrementResult), flags);
    }

    void wordINC(ExecutionContext executionContext) {
        int wordData = executionContext.getData();
        int incrementResult = ++wordData & WORD_MASK;
        executionContext.setData(incrementResult);
        logALUOperation("Word Increment", StringUtils.toHex(incrementResult),
                executionContext.registers.getFlags());
    }

    void wordDEC(ExecutionContext executionContext) {
        int wordData = executionContext.getData();
        int decrementResult = --wordData & WORD_MASK;
        executionContext.setData(decrementResult);
        logALUOperation("Word Decrement", StringUtils.toHex(decrementResult),
                executionContext.registers.getFlags());
    }

    void postIncrementHL(ExecutionContext executionContext) {
        int HL = executionContext.registers.getHL();
        int incrementResult = ++HL & WORD_MASK;
        executionContext.registers.setHL(incrementResult);
        logALUOperation("HL PostIncrement", StringUtils.toHex(incrementResult),
                executionContext.registers.getFlags());
    }

    void postDecrementHL(ExecutionContext executionContext) {
        int HL = executionContext.registers.getHL();
        int decrementResult = --HL & WORD_MASK;
        executionContext.registers.setHL(decrementResult);
        logALUOperation("HL PostDecrement", StringUtils.toHex(decrementResult),
                executionContext.registers.getFlags());
    }

    void incrementPC(ExecutionContext executionContext, int addition) {
        int PC = executionContext.registers.getPC();
        int additionResult = (PC + addition) & WORD_MASK;
        executionContext.registers.setPC(additionResult);
    }

    void addToPC(ExecutionContext executionContext, int addition) {
        int PC = executionContext.registers.getPC();
        int additionResult = (PC + addition) & WORD_MASK;
        executionContext.registers.setPC(additionResult);
    }

    void incrementSP(ExecutionContext executionContext, int addition) {
        int SP = executionContext.registers.getSP();
        int additionResult = (SP + addition) & WORD_MASK;
        executionContext.registers.setSP(additionResult);
    }

    void addToSP(ExecutionContext executionContext) {
        int SP = executionContext.registers.getSP();
        int addition = executionContext.getData();
        int additionResult = (SP + addition) & WORD_MASK;
        executionContext.setData(additionResult);
        Flags flags = executionContext.registers.getFlags();
        flags.resetZ();
        flags.resetN();
        flags.setH(carry(SP, addition, BYTE_HALFCARRY_POSITION));
        flags.setC(carry(SP, addition, BYTE_CARRY_POSITION));
    }

    void ADD(ExecutionContext executionContext) {
        int data = executionContext.getData();
        int ADDResult = (executionContext.registers.getA() + data) & BYTE_MASK;
        executionContext.setData(ADDResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(ADDResult == 0);
        flags.resetN();
        flags.setH(carry(executionContext.registers.getA(), data, BYTE_HALFCARRY_POSITION));
        flags.setC(carry(executionContext.registers.getA(), data, BYTE_CARRY_POSITION));
        logALUOperation("ADD", StringUtils.toHex(ADDResult), flags);
    }

    void ADC(ExecutionContext executionContext) {
        int data = executionContext.getData();
        Flags flags = executionContext.registers.getFlags();
        int ADCResult =
                (executionContext.registers.getA() + data + (flags.isC() ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(ADCResult);
        flags.setZ(ADCResult == 0);
        flags.resetN();
        flags.setH(carry(executionContext.registers.getA(), data, flags.isC(),
                BYTE_HALFCARRY_POSITION));
        flags.setC(carry(executionContext.registers.getA(), data, flags.isC(),
                BYTE_CARRY_POSITION));
        logALUOperation("ADC", StringUtils.toHex(ADCResult), flags);
    }

    void SUB(ExecutionContext executionContext) {
        int data = executionContext.getData();
        int SUBResult = (executionContext.registers.getA() - data) & BYTE_MASK;
        executionContext.setData(SUBResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(SUBResult == 0);
        flags.setN();
        flags.setH(borrow(executionContext.registers.getA(), data, BYTE_HALFCARRY_POSITION));
        flags.setC(borrow(executionContext.registers.getA(), data, BYTE_CARRY_POSITION));
        logALUOperation("SUB", StringUtils.toHex(SUBResult), flags);
    }

    void SBC(ExecutionContext executionContext) {
        int data = executionContext.getData();
        Flags flags = executionContext.registers.getFlags();
        int SBCResult =
                (executionContext.registers.getA() - data - (flags.isC() ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(SBCResult);
        flags.setZ(SBCResult == 0);
        flags.setN();
        flags.setH(borrow(executionContext.registers.getA(), data, flags.isC(),
                BYTE_HALFCARRY_POSITION));
        flags.setC(borrow(executionContext.registers.getA(), data, flags.isC(),
                BYTE_CARRY_POSITION));
        logALUOperation("SBC", StringUtils.toHex(SBCResult), flags);
    }

    void AND(ExecutionContext executionContext) {
        int ANDResult =
                (executionContext.getData() & executionContext.registers.getA()) & BYTE_MASK;
        executionContext.setData(ANDResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(ANDResult == 0);
        flags.resetN();
        flags.setH();
        flags.resetC();
        logALUOperation("AND", StringUtils.toHex(ANDResult), flags);
    }

    void XOR(ExecutionContext executionContext) {
        int XORResult =
                (executionContext.getData() ^ executionContext.registers.getA()) & BYTE_MASK;
        executionContext.setData(XORResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(XORResult == 0);
        flags.resetN();
        flags.resetH();
        flags.resetC();
        logALUOperation("XOR", StringUtils.toHex(XORResult), flags);
    }

    void OR(ExecutionContext executionContext) {
        int ORResult = (executionContext.getData() | executionContext.registers.getA()) & BYTE_MASK;
        executionContext.setData(ORResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(ORResult == 0);
        flags.resetN();
        flags.resetH();
        flags.resetC();
        logALUOperation("OR", StringUtils.toHex(ORResult), flags);
    }

    void CP(ExecutionContext executionContext) {
        int CPResult = (executionContext.registers.getA() - executionContext.getData()) & BYTE_MASK;
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(CPResult == 0);
        flags.setN();
        flags.setH(borrow(executionContext.registers.getA(), executionContext.getData(),
                BYTE_HALFCARRY_POSITION));
        flags.setC(executionContext.registers.getA() < executionContext.getData());
        logALUOperation("CP", StringUtils.toHex(CPResult), flags);
    }

    void wordADD(ExecutionContext executionContext) {
        int data = executionContext.getData();
        int ADDResult = (executionContext.registers.getHL() + data) & WORD_MASK;
        executionContext.setData(ADDResult);
        Flags flags = executionContext.registers.getFlags();
        flags.resetN();
        flags.setH(carry(executionContext.registers.getHL(), data, WORD_HALFCARRY_POSITION));
        flags.setC(carry(executionContext.registers.getHL(), data, WORD_CARRY_POSITION));
        logALUOperation("WordADD", StringUtils.toHex(ADDResult), flags);
    }
    
    void DAA(ExecutionContext executionContext) {
        int adjust = 0;
        int A = executionContext.registers.getA();
        Flags flags = executionContext.registers.getFlags();
        boolean N = flags.isN();
        boolean H = flags.isH();
        boolean C = flags.isC();
        boolean scheduledCarry = false;

        if (H || (!N && (A & 0xF) > 0x9)) {
            adjust += 0x6;
        }
        if (C || (!N && A > 0x99)) {
            adjust += 0x60;
            scheduledCarry = true;
        }

        int DAAResult = (A + (N ? -adjust : adjust)) & BYTE_MASK;
        executionContext.setData(DAAResult);
        flags.setZ(DAAResult == 0);
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("DAA", StringUtils.toHex(DAAResult), flags);
    }

    void CPL(ExecutionContext executionContext) {
        int A = executionContext.registers.getA();
        int CPLResult = ~A & BYTE_MASK;
        executionContext.setData(CPLResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setN();
        flags.setH();
        logALUOperation("CPL", StringUtils.toHex(CPLResult), executionContext.registers.getFlags());
    }

    void SCF(ExecutionContext executionContext) {
        Flags flags = executionContext.registers.getFlags();
        flags.resetN();
        flags.resetH();
        flags.setC();
        logALUOperation("SCF", flags.isC(), flags);
    }

    void CCF(ExecutionContext executionContext) {
        Flags flags = executionContext.registers.getFlags();
        boolean C = flags.isC();
        flags.resetN();
        flags.resetH();
        flags.setC(!C);
        logALUOperation("CCF", flags.isC(), flags);
    }

    void RLA(ExecutionContext executionContext) {
        int data = executionContext.registers.getA();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        Flags flags = executionContext.registers.getFlags();
        int RLResult = ((data << 1) | (flags.isC() ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(RLResult);
        flags.resetZ();
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RLA", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    void RLCA(ExecutionContext executionContext) {
        int data = executionContext.registers.getA();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        Flags flags = executionContext.registers.getFlags();
        int RLResult = ((data << 1) | (scheduledCarry ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(RLResult);
        flags.resetZ();
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RLCA", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    void RL(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        Flags flags = executionContext.registers.getFlags();
        int RLResult = ((data << 1) | (flags.isC() ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(RLResult);
        flags.setZ(RLResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RL", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    void RLC(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        Flags flags = executionContext.registers.getFlags();
        int RLResult = ((data << 1) | (scheduledCarry ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(RLResult);
        flags.setZ(RLResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RLC", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    void RRA(ExecutionContext executionContext) {
        int data = executionContext.registers.getA();
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int RRResult = (flags.isC() ? ByteUtils.setBit(data >> 1, 7) : data >> 1) & BYTE_MASK;
        executionContext.setData(RRResult);
        flags.resetZ();
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RRA", StringUtils.toHex(RRResult), executionContext.registers.getFlags());
    }

    void RRCA(ExecutionContext executionContext) {
        int data = executionContext.registers.getA();
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int RRResult = (scheduledCarry ? ByteUtils.setBit(data >> 1, 7) : data >> 1) & BYTE_MASK;
        executionContext.setData(RRResult);
        flags.resetZ();
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RRCA", StringUtils.toHex(RRResult), executionContext.registers.getFlags());
    }

    void RR(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int RRResult = (flags.isC() ? ByteUtils.setBit(data >> 1, 7) : data >> 1) & BYTE_MASK;
        executionContext.setData(RRResult);
        flags.setZ(RRResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RR", StringUtils.toHex(RRResult), executionContext.registers.getFlags());
    }

    void RRC(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int RRResult = (scheduledCarry ? ByteUtils.setBit(data >> 1, 7) : data >> 1) & BYTE_MASK;
        executionContext.setData(RRResult);
        flags.setZ(RRResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("RRC", StringUtils.toHex(RRResult), executionContext.registers.getFlags());
    }

    void SLA(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        Flags flags = executionContext.registers.getFlags();
        int SLAResult = ByteUtils.resetBit(data << 1, 0) & BYTE_MASK;
        executionContext.setData(SLAResult);
        flags.setZ(SLAResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("SLA", StringUtils.toHex(SLAResult), executionContext.registers.getFlags());
    }

    void SRA(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledMsb = ByteUtils.getBit(data, 7);
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int SRAResult = (scheduledMsb ? ByteUtils.setBit(data >> 1, 7) : data >> 1) & BYTE_MASK;
        executionContext.setData(SRAResult);
        flags.setZ(SRAResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("SRA", StringUtils.toHex(SRAResult), executionContext.registers.getFlags());
    }

    void SWAP(ExecutionContext executionContext) {
        int data = executionContext.getData();
        int SWAPResult = (data << 4 & 0xF0) | (data >> 4 & 0x0F);
        executionContext.setData(SWAPResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(SWAPResult == 0);
        flags.resetN();
        flags.resetH();
        flags.resetC();
        logALUOperation("SWAP", StringUtils.toHex(SWAPResult), executionContext.registers.getFlags());
    }

    void SRL(ExecutionContext executionContext) {
        int data = executionContext.getData();
        boolean scheduledCarry = ByteUtils.getBit(data, 0);
        Flags flags = executionContext.registers.getFlags();
        int SRLResult = ByteUtils.resetBit(data >> 1, 7) & BYTE_MASK;
        executionContext.setData(SRLResult);
        flags.setZ(SRLResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        logALUOperation("SRL", StringUtils.toHex(SRLResult), executionContext.registers.getFlags());
    }

    void BIT(ExecutionContext executionContext, int bit) {
        boolean testBit = ByteUtils.getBit(executionContext.getData(), bit);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(!testBit);
        flags.resetN();
        flags.setH();
        logALUOperation("BIT " + bit, testBit, flags);
    }

    void RES(ExecutionContext executionContext, int bit) {
        int data = executionContext.getData();
        int RESResult = ByteUtils.resetBit(data, bit);
        executionContext.setData(RESResult);
        Flags flags = executionContext.registers.getFlags();
        logALUOperation("RES " + bit, RESResult, flags);
    }

    void SET(ExecutionContext executionContext, int bit) {
        int data = executionContext.getData();
        int SETResult = ByteUtils.setBit(data, bit);
        executionContext.setData(SETResult);
        Flags flags = executionContext.registers.getFlags();
        logALUOperation("SET " + bit, SETResult, flags);
    }

    private boolean carry(int a, int b, int position) {
        int mask = (1 << position + 1) - 1;
        boolean carry = (a & mask) + (b & mask) >> position + 1 != 0;
        return carry;
    }

    private boolean carry(int a, int b, boolean CY, int position) {
        int mask = (1 << position + 1) - 1;
        boolean carry = (a & mask) + (b & mask) + (CY ? 1 : 0) >> position + 1 != 0;
        return carry;
    }

    private boolean borrow(int a, int b, int position) {
        int mask = (1 << position + 1) - 1;
        boolean borrow = (a & mask) < (b & mask);
        return borrow;
    }

    private boolean borrow(int a, int b, boolean CY, int position) {
        int mask = (1 << position + 1) - 1;
        boolean borrow = (a & mask) < ((b & mask) + (CY ? 1 : 0));
        return borrow;
    }

    private void logALUOperation(String operation, Object result, Flags flags) {
        logger.trace("{} = {}. Flags = {} .", operation, result, flags);
    }

}
