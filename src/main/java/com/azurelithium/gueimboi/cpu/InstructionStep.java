package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.ByteUtils;
import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class InstructionStep {

    final Logger logger = LoggerFactory.getLogger(getClass());

    abstract void execute(ExecutionContext executionContext);

}


interface PushOperation {

    default void push(ExecutionContext executionContext, int[] bytes) {
        int pushAddress = executionContext.registers.getSP();
        executionContext.MMU.writeBytes(pushAddress - bytes.length, bytes); // 8 cycles
        executionContext.ALU.incrementSP(executionContext, -bytes.length);
    }

}


interface PopOperation {

    default int[] pop(ExecutionContext executionContext) {
        int popAddress = executionContext.registers.getSP();
        int[] bytes = executionContext.MMU.readBytes(popAddress, Short.BYTES); // 8 cycles                                                                                          // cycles
        executionContext.ALU.incrementSP(executionContext, bytes.length);
        return bytes;
    }

}


class Load extends InstructionStep {

    private ReadableWritable operand;

    Load(ReadableWritable _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        int data = operand.read(executionContext);
        setData(executionContext, data);
    }

    void setData(ExecutionContext executionContext, int data) {
        executionContext.setData(data);
    }

}


class Store extends InstructionStep {

    private ReadableWritable operand;

    Store(ReadableWritable _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        operand.write(executionContext);
    }

}


class Push extends InstructionStep implements PushOperation {

    void execute(ExecutionContext executionContext) {
        int[] dataBytes = executionContext.getDataBytes();
        push(executionContext, dataBytes);
        logger.trace("Push {}.", StringUtils.toHex(executionContext.getData()));
    }

}


class Pop extends InstructionStep implements PopOperation {

    void execute(ExecutionContext executionContext) {
        int[] dataBytes = pop(executionContext);
        executionContext.setData(ByteUtils.toWord(dataBytes[0], dataBytes[1]));
        logger.trace("Pop {}.", StringUtils.toHex(executionContext.getData()));
    }

}


class ByteALUIncrement extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.byteIncrement(executionContext);
    }

}


class ByteALUDecrement extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.byteDecrement(executionContext);
    }

}


class WordALUIncrement extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.wordIncrement(executionContext);
    }

}


class WordALUDecrement extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.wordDecrement(executionContext);
    }

}


class PostIncrementHL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.postIncrementHL(executionContext);
    }

}


class PostDecrementHL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.postDecrementHL(executionContext);
    }

}


class ADD extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.ADD(executionContext);
    }

}


class SUB extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SUB(executionContext);
    }

}


class XOR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.XOR(executionContext);
    }

}


class CP extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.CP(executionContext);
    }

}


class RLA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RLA(executionContext);
    }

}


class RL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RL(executionContext);
    }

}


class TestBit extends InstructionStep {

    private int bit;

    TestBit(int _bit) {
        bit = _bit;
    }

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.testBit(executionContext, bit);
    }

}


class IfZ extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        if (!executionContext.registers.getFlags().isZ()) {
            executionContext.executeNextStep = false;
        }
    }

}


class IfNZ extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        if (executionContext.registers.getFlags().isZ()) {
            executionContext.executeNextStep = false;
        }
    }

}


class JumpRelative extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        int relativeJump = executionContext.getData();
        executionContext.ALU.addToPC(executionContext, relativeJump);
        logger.trace("JR: {} to address {}.", relativeJump,
                StringUtils.toHex(executionContext.registers.getPC()));
    }

}


class Call extends InstructionStep implements PushOperation {

    void execute(ExecutionContext executionContext) {
        int callAddress = executionContext.getDataAddress();
        int returnAddress = executionContext.registers.getPC();
        int[] returnAddressBytes =
                new int[] {ByteUtils.getMSB(returnAddress), ByteUtils.getLSB(returnAddress)};
        push(executionContext, returnAddressBytes);
        executionContext.registers.setPC(callAddress);
        logger.trace("CALL: {}, return address is {}.", StringUtils.toHex(callAddress),
                StringUtils.toHex(returnAddress));
    }

}


class Ret extends InstructionStep implements PopOperation {

    void execute(ExecutionContext executionContext) {
        int[] returnAddressBytes = pop(executionContext);
        int returnAddress = ByteUtils.toWord(returnAddressBytes[0], returnAddressBytes[1]);
        executionContext.registers.setPC(returnAddress);
        logger.trace("RET: {}.", StringUtils.toHex(returnAddress));
    }

}



