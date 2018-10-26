package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class InstructionStep {

    final Logger logger = LoggerFactory.getLogger(getClass());

    abstract void execute(ExecutionContext executionContext);

}


class Load extends InstructionStep {

    private Readable operand;

    Load(Readable _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        int data = operand.read(executionContext);
        setData(executionContext, data);
    }

    void setData(ExecutionContext executionContext, int data) {
        executionContext.setPrimaryData(data);
    }

}


class LoadSecundary extends Load {

    LoadSecundary(Readable operand) {
        super(operand);
    }

    void setData(ExecutionContext executionContext, int data) {
        executionContext.setSecondaryData(data);
    }

}


class Store extends InstructionStep {

    private Writable operand;

    Store(Writable _operand) {
        operand = _operand;
    }

    void execute(ExecutionContext executionContext) {
        operand.write(executionContext);
    }

}


class Increment extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        int data = executionContext.getPrimaryData();
        executionContext.setPrimaryData(++data);
    }

}


class Decrement extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        int data = executionContext.getPrimaryData();
        executionContext.setPrimaryData(--data);
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
        int relativeJump = executionContext.getPrimaryData();
        executionContext.registers.incrementPC(relativeJump);
        logger.trace("Relative jump of {} to address {}.", relativeJump,
                StringUtils.toHex(executionContext.registers.getPC()));
    }

}
