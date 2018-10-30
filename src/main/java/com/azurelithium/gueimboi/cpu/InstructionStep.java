package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class InstructionStep {

    final Logger logger = LoggerFactory.getLogger(getClass());

    abstract void execute(ExecutionContext executionContext);

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


class XOR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.XOR(executionContext);
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
        logger.trace("Relative jump of {} to address {}.", relativeJump,
                StringUtils.toHex(executionContext.registers.getPC()));
    }

}
