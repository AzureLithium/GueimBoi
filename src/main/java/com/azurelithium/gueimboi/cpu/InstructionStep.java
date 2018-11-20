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


class INC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.INC(executionContext);
    }

}


class DEC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.DEC(executionContext);
    }

}


class WordINC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.wordINC(executionContext);
    }

}


class WordDEC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.wordDEC(executionContext);
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


class ADC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.ADC(executionContext);
    }

}


class SUB extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SUB(executionContext);
    }

}


class SBC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SBC(executionContext);
    }

}


class AND extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.AND(executionContext);
    }

}


class XOR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.XOR(executionContext);
    }

}


class OR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.OR(executionContext);
    }

}


class CP extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.CP(executionContext);
    }

}


class WordADD extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.wordADD(executionContext);
    }

}


class DAA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.DAA(executionContext);
    }

}


class CPL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.CPL(executionContext);
    }

}


class SCF extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SCF(executionContext);
    }

}


class CCF extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.CCF(executionContext);
    }

}


class RLA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RLA(executionContext);
    }

}


class RLCA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RLCA(executionContext);
    }

}


class RRA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RRA(executionContext);
    }

}


class RRCA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RRCA(executionContext);
    }

}


class RL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RL(executionContext);
    }

}


class RLC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RLC(executionContext);
    }

}


class RR extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RR(executionContext);
    }

}


class RRC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RRC(executionContext);
    }

}


class SLA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SLA(executionContext);
    }

}


class SRA extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SRA(executionContext);
    }

}


class SWAP extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SWAP(executionContext);
    }

}


class SRL extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SRL(executionContext);
    }

}


class AddToSP extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.addToSP(executionContext);
    }

}


class BIT extends InstructionStep {

    private int bit;

    BIT(int _bit) {
        bit = _bit;
    }

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.BIT(executionContext, bit);
    }

}


class RES extends InstructionStep {

    private int bit;

    RES(int _bit) {
        bit = _bit;
    }

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.RES(executionContext, bit);
    }

}


class SET extends InstructionStep {

    private int bit;

    SET(int _bit) {
        bit = _bit;
    }

    void execute(ExecutionContext executionContext) {
        executionContext.ALU.SET(executionContext, bit);
    }

}


class IfNZ extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.executeNextStep = !executionContext.registers.getFlags().isZ();
    }

}


class IfZ extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.executeNextStep = executionContext.registers.getFlags().isZ();
    }

}


class IfNC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.executeNextStep = !executionContext.registers.getFlags().isC();
    }

}


class IfC extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.executeNextStep = executionContext.registers.getFlags().isC();
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


class RST extends InstructionStep {

    int destination;

    RST(int _destination) {
        destination = _destination;
    }

    void execute(ExecutionContext executionContext) {
        int jump = destination;
        executionContext.registers.setPC(jump);
        logger.trace("RST: {} .", StringUtils.toHex(jump));
    }

}


class InternalDelay extends InstructionStep {

    InternalDelay() {
        consumesCycles = true;
    }

    void execute(ExecutionContext executionContext) {}

}


class UseDataAsAddress extends InstructionStep {

    void execute(ExecutionContext executionContext) {
        executionContext.setDataAddress(executionContext.getData());
    }

}
