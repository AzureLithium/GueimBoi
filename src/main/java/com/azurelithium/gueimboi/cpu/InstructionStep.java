package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class InstructionStep {

    final Logger logger = LoggerFactory.getLogger(getClass());

    protected boolean consumesCycles = false;
    boolean doesConsumeCycles() {
        return consumesCycles;
    }

    abstract void execute(ExecutionContext executionContext);

}


class DecodeLSB extends InstructionStep {

    private Decodable operand;

    DecodeLSB(Decodable _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.decodeLSB(executionContext);
    }

}


class DecodeMSB extends InstructionStep {

    private DecodableWord operand;

    DecodeMSB(DecodableWord _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.decodeMSB(executionContext);
    }

}


class Load extends InstructionStep {

    private ReadableWritable operand;

    Load(ReadableWritable _operand) {
        operand = _operand;
        consumesCycles = operand.consumesCycles();
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
        consumesCycles = operand.consumesCycles();
    }

    void execute(ExecutionContext executionContext) {
        operand.write(executionContext);
    }

}


class StoreLSB extends InstructionStep {

    private AddressOperand operand;

    StoreLSB(AddressOperand _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.writeLSB(executionContext);
    }

}


class StoreMSB extends InstructionStep {

    private AddressOperand operand;

    StoreMSB(AddressOperand _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.writeMSB(executionContext);
    }

}


class PushLSB extends InstructionStep {

    private PushablePopable operand;

    PushLSB(PushablePopable _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.pushLSB(executionContext);
    }

}


class PushMSB extends InstructionStep {

    private PushablePopable operand;

    PushMSB(PushablePopable _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.pushMSB(executionContext);
    }

}


class PopLSB extends InstructionStep {

    private PushablePopable operand;

    PopLSB(PushablePopable _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.popLSB(executionContext);
    }

}


class PopMSB extends InstructionStep {

    private PushablePopable operand;

    PopMSB(PushablePopable _operand) {
        operand = _operand;
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
        operand.popMSB(executionContext);
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


class Jump extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        int jump = executionContext.getDataAddress();
        executionContext.registers.setPC(jump);
        logger.trace("JP: {} .", StringUtils.toHex(jump));
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


class InternalDelay extends InstructionStep {

    InternalDelay() {
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {
    }

}