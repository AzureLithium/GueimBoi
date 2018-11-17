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

    void byteIncrement(ExecutionContext executionContext) {
        int byteData = executionContext.getData();
        int incrementResult = (byteData + 1) & BYTE_MASK;
        executionContext.setData(incrementResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(incrementResult == 0);
        flags.resetN();
        flags.setH(carry(byteData, 1, BYTE_HALFCARRY_POSITION));
        logALUOperation("Byte Increment", StringUtils.toHex(incrementResult), flags);
    }

    void byteDecrement(ExecutionContext executionContext) {
        int byteData = executionContext.getData();
        int decrementResult = (byteData - 1) & BYTE_MASK;
        executionContext.setData(decrementResult);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(decrementResult == 0);
        flags.setN();
        flags.setH(borrow(byteData, 1, BYTE_HALFCARRY_POSITION));
        logALUOperation("Byte Decrement", StringUtils.toHex(decrementResult), flags);
    }

    void wordIncrement(ExecutionContext executionContext) {
        int wordData = executionContext.getData();
        int incrementResult = ++wordData & WORD_MASK;
        executionContext.setData(incrementResult);
        logALUOperation("Word Increment", StringUtils.toHex(incrementResult),
                executionContext.registers.getFlags());
    }

    void wordDecrement(ExecutionContext executionContext) {
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
        // flags for OPCODES 0xE8 and 0xF8 should be set here
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

    void RLA(ExecutionContext executionContext) {
        int data = executionContext.registers.getA();
        int RLResult = rotateLeft(executionContext, data);
        logALUOperation("RLA", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    void RL(ExecutionContext executionContext) {
        int data = executionContext.getData();
        int RLResult = rotateLeft(executionContext, data);
        logALUOperation("RL", StringUtils.toHex(RLResult), executionContext.registers.getFlags());
    }

    private int rotateLeft(ExecutionContext executionContext, int data) {
        Flags flags = executionContext.registers.getFlags();
        boolean scheduledCarry = ByteUtils.getBit(data, 7);
        int RLResult = ((data << 1) | (flags.isC() ? 1 : 0)) & BYTE_MASK;
        executionContext.setData(RLResult);
        flags.setZ(RLResult == 0);
        flags.resetN();
        flags.resetH();
        flags.setC(scheduledCarry);
        return RLResult;
    }

    void testBit(ExecutionContext executionContext, int bit) {
        boolean testBit = ByteUtils.getBit(executionContext.getData(), bit);
        Flags flags = executionContext.registers.getFlags();
        flags.setZ(!testBit);
        flags.resetN();
        flags.setH();
        logALUOperation("BIT " + bit, testBit, flags);
    }

    private boolean carry(int a, int b, int position) {
        int mask = (1 << position + 1) - 1;
        boolean carry = (a & mask) + (b & mask) >> position + 1 != 0;
        return carry;
    }

    private boolean borrow(int a, int b, int position) {
        int mask = (1 << position + 1) - 1;
        boolean borrow = (a & mask) < (b & mask);
        return borrow;
    }

    private void logALUOperation(String operation, Object result, Flags flags) {
        logger.trace("{} = {}. Flags = {} .", operation, result, flags);
    }

}
