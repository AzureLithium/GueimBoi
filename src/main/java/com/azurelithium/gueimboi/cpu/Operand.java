package com.azurelithium.gueimboi.cpu;

import com.azurelithium.gueimboi.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Operand {

    final Logger logger = LoggerFactory.getLogger(getClass());

    String getName() {
        return getClass().getSimpleName();
    }

}

interface Decodable {
    void logDecode(String data, String address);
    void decodeLSB(ExecutionContext executionContext);
}

interface DecodableByte extends Decodable {
}

interface DecodableWord extends Decodable {
    void decodeMSB(ExecutionContext executionContext);
}

interface ReadableWritable {
    int read(ExecutionContext executionContext);
    void write(ExecutionContext executionContext);
    default boolean consumesCycles() {
        return false;
    }
}

interface PushablePopable {

    default void push(ExecutionContext executionContext, int byteWrote) {
        int pushAddress = executionContext.registers.getSP();
        executionContext.MMU.writeByte(pushAddress - Byte.BYTES, byteWrote);
        executionContext.ALU.incrementSP(executionContext, -Byte.BYTES);
    }

    default int pop(ExecutionContext executionContext) {
        int popAddress = executionContext.registers.getSP();
        int byteRead = executionContext.MMU.readByte(popAddress); // cycles
        executionContext.ALU.incrementSP(executionContext, Byte.BYTES);
        return byteRead;
    }

    void pushLSB(ExecutionContext executionContext);

    void pushMSB(ExecutionContext executionContext);

    void popLSB(ExecutionContext executionContext);

    void popMSB(ExecutionContext executionContext);

}

abstract class RegisterOperand extends Operand implements ReadableWritable {

    abstract int getRegisterValue(ExecutionContext executionContext);

    abstract void setRegisterValue(ExecutionContext executionContext);

    public int read(ExecutionContext executionContext) {
        int registerValue = getRegisterValue(executionContext);
        logReadFromRegister(StringUtils.toHex(registerValue));
        return registerValue;
    }

    public void write(ExecutionContext executionContext) {
        setRegisterValue(executionContext);
        logWriteToRegister(executionContext.printData());
    }

    void logReadFromRegister(String data) {
        logger.trace("Registers: Reading data {} from register {}.", data, getClass().getSimpleName());
    }

    void logWriteToRegister(String data) {
        logger.trace("Registers: Writing data {} to register {}.", data, getClass().getSimpleName());
    }

}

abstract class AddressOperand extends Operand implements ReadableWritable {

    public boolean consumesCycles() {
        return true;
    }

    void setAddress(ExecutionContext executionContext) {
    };

    public int read(ExecutionContext executionContext) {
        setAddress(executionContext);
        int byteRead = executionContext.MMU.readByte(executionContext.getDataAddress());
        logReadByteFromAddress(StringUtils.toHex(byteRead), executionContext.printDataAddress());
        return byteRead;
    }

    public void write(ExecutionContext executionContext) {
        setAddress(executionContext);
        executionContext.MMU.writeByte(executionContext.getDataAddress(), executionContext.getDataLSB());
        logWriteByteToAddress(StringUtils.toHex(executionContext.getDataLSB()), executionContext.printDataAddress());
    }

    public void writeLSB(ExecutionContext executionContext) {
        write(executionContext);
    }

    public void writeMSB(ExecutionContext executionContext) {
        setAddress(executionContext);
        executionContext.MMU.writeByte(executionContext.getDataAddress() + 1, executionContext.getDataMSB());
        logWriteByteToAddress(StringUtils.toHex(executionContext.getDataMSB()),
                StringUtils.toHex(executionContext.getDataAddress() + 1));
    }

    void logReadByteFromAddress(String data, String address) {
        logger.trace("Memory: Read byte {} from address {}.", data, address);
    }

    void logWriteByteToAddress(String data, String address) {
        logger.trace("Memory: Writing bytes {} to address {}.", data, address);
    }

}

final class A extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getA();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setA(executionContext.getData());
    }

}

final class B extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getB();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setB(executionContext.getData());
    }

}

final class C extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setC(executionContext.getData());
    }

}

final class CAddress extends AddressOperand {

    String getName() {
        return "(C)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.setDataAddress(0xFF00 | executionContext.registers.getC());
    }

}

final class D extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getD();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setD(executionContext.getData());
    }

}

final class E extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getE();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setE(executionContext.getData());
    }

}

final class H extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getH();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setH(executionContext.getData());
    }

}

final class L extends RegisterOperand {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getL();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setL(executionContext.getData());
    }

}

final class AF extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getAF();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setAF(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getFlags().getFlags());
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getA());
    }

    public void popLSB(ExecutionContext executionContext) {
        executionContext.registers.setFlags(pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        executionContext.registers.setA(pop(executionContext));
    }

}

final class BC extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getBC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setBC(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getC());
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getB());
    }

    public void popLSB(ExecutionContext executionContext) {
        executionContext.registers.setC(pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        executionContext.registers.setB(pop(executionContext));
    }

}

final class BCAddress extends AddressOperand {

    String getName() {
        return "(BC)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.setDataAddress(executionContext.registers.getBC());
    }

}

final class DE extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getDE();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setDE(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getE());
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getD());
    }

    public void popLSB(ExecutionContext executionContext) {
        executionContext.registers.setE(pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        executionContext.registers.setD(pop(executionContext));
    }

}

final class DEAddress extends AddressOperand {

    String getName() {
        return "(DE)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.setDataAddress(executionContext.registers.getDE());
    }

}

final class HL extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getHL();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setHL(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getL());
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getH());
    }

    public void popLSB(ExecutionContext executionContext) {
        executionContext.registers.setL(pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        executionContext.registers.setH(pop(executionContext));
    }

}

final class HLAddress extends AddressOperand {

    String getName() {
        return "(HL)";
    }

    void setAddress(ExecutionContext executionContext) {
        executionContext.setDataAddress(executionContext.registers.getHL());
    }

}

final class PC extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getPC();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setPC(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getPC() & 0xFF);
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getPC() >> Byte.SIZE);
    }

    public void popLSB(ExecutionContext executionContext) {
        int PC = executionContext.registers.getPC();
        executionContext.registers.setPC((PC & 0xFF00) | pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        int PC = executionContext.registers.getPC();
        executionContext.registers.setPC((pop(executionContext) << Byte.SIZE) | (PC & 0xFF));
    }

}

final class SP extends RegisterOperand implements PushablePopable {

    int getRegisterValue(ExecutionContext executionContext) {
        return executionContext.registers.getSP();
    }

    void setRegisterValue(ExecutionContext executionContext) {
        executionContext.registers.setSP(executionContext.getData());
    }

    public void pushLSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getSP() & 0xFF);
    }

    public void pushMSB(ExecutionContext executionContext) {
        push(executionContext, executionContext.registers.getSP() >> Byte.SIZE);
    }

    public void popLSB(ExecutionContext executionContext) {
        int SP = executionContext.registers.getSP();
        executionContext.registers.setSP((SP & 0xFF00) | pop(executionContext));
    }

    public void popMSB(ExecutionContext executionContext) {
        int SP = executionContext.registers.getSP();
        executionContext.registers.setSP((pop(executionContext) << Byte.SIZE) | (SP & 0xFF));
    }

}

final class ByteImmediate extends Operand implements DecodableByte {

    String getName() {
        return "d8";
    }

    public void decodeLSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setData(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched byte operand {} from address {}.", data, address);
    }

}

final class ByteAddress extends AddressOperand implements DecodableByte {

    String getName() {
        return "(a8)";
    }

    public void decodeLSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setDataAddress(0xFF00 | byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(StringUtils.toHex(byteRead), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched byte address operand {} from address {}.", data, address);
    }

}

final class SignedByteImmediate extends Operand implements DecodableByte {

    String getName() {
        return "r8";
    }

    public void decodeLSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = (byte) executionContext.MMU.readByte(address);
        executionContext.setData(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched signed byte operand {} from address {}.", data, address);
    }

}

final class WordImmediate extends Operand implements DecodableWord {

    String getName() {
        return "d16";
    }

    public void decodeLSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setData(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
    }

    public void decodeMSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        int data = executionContext.getData();
        executionContext.setData((byteRead << Byte.SIZE) + data);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address - 1));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word operand {} from address {}.", data, address);
    }

}

final class WordAddress extends AddressOperand implements DecodableWord {

    String getName() {
        return "(a16)";
    }

    public void decodeLSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        executionContext.setDataAddress(byteRead);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
    }

    public void decodeMSB(ExecutionContext executionContext) {
        int address = executionContext.registers.getPC();
        int byteRead = executionContext.MMU.readByte(address);
        int dataAddress = executionContext.getDataAddress();
        executionContext.setDataAddress((byteRead << Byte.SIZE) + dataAddress);
        executionContext.ALU.incrementPC(executionContext, Byte.BYTES);
        logDecode(executionContext.printData(), StringUtils.toHex(address - 1));
    }

    public void logDecode(String data, String address) {
        logger.trace("Decoding: Fetched word address operand {} from address {}.", data, address);
    }

}
